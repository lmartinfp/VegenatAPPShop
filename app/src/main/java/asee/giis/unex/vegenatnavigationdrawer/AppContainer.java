package asee.giis.unex.vegenatnavigationdrawer;

import android.content.Context;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.network.ProductNetworkDataSource;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;
import asee.giis.unex.vegenatnavigationdrawer.roomdb.VegenatDatabase;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.EditUserViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.LoginActivityViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.MainActivityViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.OrderFragmentViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductDetailsActivityViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductFragmentViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductShoppingListViewModelFactory;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.RegisterActivityViewModelFactory;

/**
 * Contenedor de depencias para el acceso a datos desde el repository (BD y API) y los ViewModels
 **/

public class AppContainer {

    //Base de datos de Room
    private VegenatDatabase vegenatDatabase;
    //API de Network
    private ProductNetworkDataSource productNetworkDataSource;
    //Repository con las 2 fuentes de datos
    public VegenatRepository vegenatRepository;
    //Factories con todos los ViewModels
    public ProductFragmentViewModelFactory factoryProductFragmentFactory;
    public ProductDetailsActivityViewModelFactory factoryProductDetailsActivityViewModelFactory;
    public LoginActivityViewModelFactory loginActivityViewModelFactory;
    public RegisterActivityViewModelFactory registerActivityViewModelFactory;
    public ProductShoppingListViewModelFactory productShoppingListViewModelFactory;
    public EditUserViewModelFactory editUserViewModelFactory;
    public OrderFragmentViewModelFactory orderFragmentViewModelFactory;
    public MainActivityViewModelFactory mainActivityViewModelFactory;
    //Dependencias para el usuario y poder hacer filtrados
    public String username; //usuario para la sesión
    public long id_user; //id_user para la sesión

    public AppContainer (Context context) {
        vegenatDatabase = VegenatDatabase.getInstance(context); //instanciamos la bd
        productNetworkDataSource = ProductNetworkDataSource.getInstance(); //instanciamos el product network para descargar los productos de la API

        //Instanciamos el vegenatRepository con todos los DAO's para acceder de la base de datos instanciada y el productNetwork
        vegenatRepository = VegenatRepository.getInstance(vegenatDatabase.getOrderDAO(), vegenatDatabase.getProductShoppingListDAO(),
                                                          vegenatDatabase.getUserDAO(), vegenatDatabase.getProductDAO(), productNetworkDataSource);

        //Creamos los factories (le tenemos que pasar a todas el Repository para poder acceder a los datos)
        factoryProductFragmentFactory = new ProductFragmentViewModelFactory(vegenatRepository);
        factoryProductDetailsActivityViewModelFactory = new ProductDetailsActivityViewModelFactory(vegenatRepository);
        loginActivityViewModelFactory = new LoginActivityViewModelFactory(vegenatRepository);
        registerActivityViewModelFactory = new RegisterActivityViewModelFactory(vegenatRepository);
        editUserViewModelFactory = new EditUserViewModelFactory(vegenatRepository);
        productShoppingListViewModelFactory = new ProductShoppingListViewModelFactory(vegenatRepository);
        orderFragmentViewModelFactory = new OrderFragmentViewModelFactory(vegenatRepository);
        mainActivityViewModelFactory = new MainActivityViewModelFactory(vegenatRepository);
    }
}
