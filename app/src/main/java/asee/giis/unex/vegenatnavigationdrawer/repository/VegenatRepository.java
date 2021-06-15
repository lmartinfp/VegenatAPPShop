package asee.giis.unex.vegenatnavigationdrawer.repository;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Comment;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.network.ProductNetworkDataSource;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.CommentDAO;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.OrderDAO;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.ProductDAO;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.ProductShoppingListDAO;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.UserDAO;


/**
 * Handles data operations in Sunshine. Acts as a mediator between {@link ProductNetworkDataSource}
 * and {@link ProductDAO}
 */
public class VegenatRepository {
    private static final String LOG_TAG = VegenatRepository.class.getSimpleName();

    // For Singleton instantiation
    private static VegenatRepository sInstance;

    //DAO de la BD
    private final ProductDAO mProductDAO;
    private final UserDAO mUserDAO;
    private final ProductShoppingListDAO mProductShoppingListDAO;
    private final OrderDAO mOrderDAO;
    private final CommentDAO mCommentDAO;

    //Fuente de datos remota
    private final ProductNetworkDataSource mProductNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();

    //Calcular la actualización de productos
    private final Map<String, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 30000;

    //LiveData
    private final MutableLiveData<Integer> idFilterProduct = new MutableLiveData<Integer>();
    private final MutableLiveData<Long> idFilterUser = new MutableLiveData<>();
    private final MutableLiveData<Long> idFilterListaCompra = new MutableLiveData<>();
    private final MutableLiveData<Long> idFilterPedidos = new MutableLiveData<>();

    //para devolver resultado en función de la situación de login o registro
    private long res;

    private VegenatRepository(OrderDAO orderDAO, CommentDAO commentDAO, ProductShoppingListDAO productShoppingListDAO,
                              UserDAO UserDAO, ProductDAO ProductDAO, ProductNetworkDataSource productNetworkDataSource) {
        mOrderDAO = orderDAO;
        mCommentDAO = commentDAO;
        mProductShoppingListDAO = productShoppingListDAO;
        mUserDAO = UserDAO;
        mProductDAO = ProductDAO;
        mProductNetworkDataSource = productNetworkDataSource;
        // LiveData that fetches repos from network
        LiveData<Product[]> networkData = mProductNetworkDataSource.getCurrentProducts();
        // As long as the products exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        networkData.observeForever(newProductsFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                //Borrar los productos de la caché
                if (newProductsFromNetwork.length > 0){ //Si existe algún producto nuevo
                    mProductDAO.deleteAllProducts();
                }
                //Insertamos en la BD local
                mProductDAO.bulkInsert(Arrays.asList(newProductsFromNetwork));
                Log.d(LOG_TAG, "New values inserted in Room");
            });
        });
    }

    public synchronized static VegenatRepository getInstance(OrderDAO orderDAO, CommentDAO commentDAO, ProductShoppingListDAO productShoppingListDAO,
                                                             UserDAO userDAO, ProductDAO dao, ProductNetworkDataSource nds) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new VegenatRepository(orderDAO, commentDAO, productShoppingListDAO, userDAO, dao, nds);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    /**
     * Database related operations
     **/

    /**
     * Obtener los productos actuales de la BD en un LiveData
     **/
    public LiveData<List<Product>> getCurrentProducts() {
        if (isFetchNeeded()) { //si necesitamos descargar productos de nuevo
            doFetchProducts(); //actualizamos
        }
        return mProductDAO.listAllProducts();
    }


    /**
     * Cambiar el id del mutablelivedata de producto para buscar por su id
     **/
    public void setIdFilterProduct (int id_product) {
        this.idFilterProduct.setValue(Integer.valueOf(id_product));
    }

    /**
     * Obtener un producto por su id en un livedata
     **/
    public LiveData<Product> getProductById() {
        return Transformations.switchMap(idFilterProduct, mProductDAO::searchById);
    }

    /**
     * Descargar los productos de la fuente de datos remota
     **/
    private void doFetchProducts(){
        Log.d(LOG_TAG, "Fetching Products from API");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mProductDAO.deleteAllProducts();
            mProductNetworkDataSource.fetchProducts();
            lastUpdateTimeMillisMap.put("1", System.currentTimeMillis());
        });
    }

    /**
     * Método que comprueba el inicio de sesión y devuelve el id del usuario en caso de haberla iniciado correctamente
     * o un número en función del fallo al iniciar sesión
     */
    public long checkSession (String username, String password) {
        User user = mUserDAO.searchUserByName(username); //obtenemos un usuario por username
        if (user == null) { //si es null devolvemos -1
            res = -1;
        }
        else {
            if (!user.getPassword().equals(password)) { //si la contraseña del usuario recuperado a través del username es distinta de la introducida
                res = 0;
            }
            else { //Si el usuario existe y la contraseña es correcta
                res = user.getId(); //devolvemos el id del usuario para meterlo en el appContainer en LoginActivity
            }
        }
        return res;
    }

    /**
     * Insertar usuario si todos los parámetros son válidos y tiene name y email únicos
     */
    public long insertUser (User user) {
        //Expresión regular  para letras minúsculas, puntos y números (formato Google más o menos)
        Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Pattern patternPassword = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$");
        Matcher matcherEmail = patternEmail.matcher(user.getEmail());
        Matcher matcherPassword = patternPassword.matcher(user.getPassword());
        if (!matcherPassword.find()) {
            return -3;
        }
        else {
            if (!matcherEmail.find()) { //Si no coincide con el patrón
                return -2;
            } else {
                if (mUserDAO.searchUserByName(user.getUsername()) != null) {
                    return -1;
                } else {
                    if (mUserDAO.searchUserByEmail(user.getEmail()) != null) {
                        return 0;
                    } else {
                        mUserDAO.insertUser(user);
                        return mUserDAO.searchUserByName(user.getUsername()).getId();
                    }
                }
            }
        }
    }

    /**
     * Modificar el MutableLiveData del usuario según el id
     */
    public void setIdUser(final long id) {
        idFilterUser.setValue(id);
    }

    /**
     * Obtener el live data de un usuaro según su id
     */
    public LiveData<User> getUserById(){
       return Transformations.switchMap(idFilterUser, mUserDAO::searchUserById);
    }

    /**
     * Editar usuario
     */
    public int editUser (User user) {
        int cambio; //En función del resultado devolvemos res (0 ó 1)
        User aux = mUserDAO.searchUserByName(user.getUsername());
        if (!user.getPassword().equals(aux.getPassword())) {
            //La contraseña no es correcta
            cambio = -2;
        }
        else { //En caso contrario
            if (user.getAddress().equals(aux.getAddress()) && user.getEmail().equals(aux.getEmail())) {
                cambio = -1;
            }
            else {
                if (!user.getEmail().equals(aux.getEmail())) {
                    User userEmail = mUserDAO.searchUserByEmail(user.getEmail());
                    if (userEmail != null) {
                        cambio = 0;
                    }
                    else {
                        Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                        Matcher matcher = patternEmail.matcher(user.getEmail());
                        if (!matcher.find()) {
                            cambio = 1;
                        }
                        else {
                            cambio = 2;
                        }
                    }
                }
                else {
                    cambio = 2;
                }
            }
        }

        if (cambio == 2) {
            mUserDAO.update(user);
        }
        return cambio;
    }

    public void deleteUser (String username, long id) {
        //Borramos pedidos, lista de la compra y usuario
        mOrderDAO.deleteOrdersByUser(id);
        mProductShoppingListDAO.deleteAllProductsShoppingList(id);
        mUserDAO.deleteUser(id);
    }

    /**
     * Añadir producto a la lista de la compra
     */
    public void addProductSoppingList(ProductWithQuantity productWithQuantity) {
        mProductShoppingListDAO.insertProductShoppingList(productWithQuantity);
    }

    /**
     * Modificar el mutablelivedata de la lista de la compra por el user id
     */
    public void setIdShoppingList (long idShoppingList) {
        this.idFilterListaCompra.setValue(idShoppingList);
    }

    /**
     * Obtener todos los productos de la lista de la compra de un usuario en un live data
     */
    public LiveData<List<ProductWithQuantity>> getShoppingListByUser() {
        //Mapea el MutableLiveData con el id del usuario a la función recoverAllProductsShoppingList
       return Transformations.switchMap(idFilterListaCompra, mProductShoppingListDAO::recoverAllProductsShoppingList);
    }

    /**
     * Borrar productos de la lista de la compra cuando se realiza el pedido
     */
    public void deleteProductShoppingListByUser (long id_user) {
        //Como no vamos a devolver ninguna lista observarble (la consulta devuelve void, no LiveData) no tenemos que hacer switchMap
        mProductShoppingListDAO.deleteAllProductsShoppingList(id_user);
    }

    public void deleteProductShoppingList (ProductWithQuantity productWithQuantity) {
        mProductShoppingListDAO.deleteProductShoppingList(productWithQuantity);
    }

    /**
     * Insertar un pedido según un id de usuario
     */
    public void insertOrder (long id_user, String username) {
        //Recuperamos la lista de la compra del usuario
        List<ProductWithQuantity> productWithQuantities = mProductShoppingListDAO.recoverAllProductsToOrder(id_user);
        float total_price = 0.0f;

        //Calculamos el precio total (unidades * precio/unidad)
        for (ProductWithQuantity pwq : productWithQuantities) {
            total_price = total_price + pwq.getPrice()*pwq.getQuantity();
        }

        //Registramos la fecha
        Date fecha = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", fecha.getTime());

        //Obtenemos el usuario para saber su dir. de entrega
        User user = mUserDAO.searchUserByName(username);

        //Creamos instancia e insertamos
        Order order = new Order(id_user, s.toString(), total_price, user.getAddress());
        mOrderDAO.insertOrder(order);
    }

    /**
     * Modificar el MutableLiveData de los pedidos según el id
     */
    public void setIdFilterOrders (long id) {
        idFilterPedidos.setValue(id);
    }

    /**
     * Obtener el observable con la lista de pedidos de un usuario
     */
    public LiveData<List<Order>> getOrdersByUser () {
        return Transformations.switchMap(idFilterPedidos, mOrderDAO::getOrdersByUser);
    }

    /**
     * Checks if we have to update the products data.
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get("1");
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;
    }

    public void insertComment(Comment comment) {
        mCommentDAO.insertComment(comment);
    }

}