package asee.giis.unex.vegenatnavigationdrawer.repository.model.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.api.ProductInterfaceAPI;
import asee.giis.unex.vegenatnavigationdrawer.executor.OnProductsLoadedListener;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsNetworkLoaderRunnable implements Runnable {

    private OnProductsLoadedListener mOnProductsLoadedListener;

    public ProductsNetworkLoaderRunnable (OnProductsLoadedListener onProductsLoadedListener) {
        mOnProductsLoadedListener = onProductsLoadedListener;
    }

    @Override
    public void run() {
        // Instanciación de Retrofit y llamada síncrona
        //LO METEMOS SIEMPRE EN UN HILO APARTE Y FUERA DEL MAIN
        //Indicamos a retrofit que use Gson para convertir y usando la url de la api de github (interfaz retrofit)
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/").addConverterFactory(GsonConverterFactory.create()).build();
        //Retrofit crea una instancia de la interfaz que he definido anteriormente
        ProductInterfaceAPI service = retrofit.create(ProductInterfaceAPI.class);
        //Hacemos metemos en un Call la llamada a la api para obtener lista de productos del servicio
        Call<List<Product>> call = service.listProducts();
        try {
            //Obtenemos uan respuesta ejecutando la llamada
            Response<List<Product>> response = call.execute();
            //Si la respuesta es nula creamos un nuevo ArrayList y sino obtenemos el cuerpo (los datos de la API)
            List <Product> products = response.body() == null ? new ArrayList<>() : response.body();
            //Llamamos el método de la interfaz en el hilo principal
            AppExecutors.getInstance().mainThread().execute(() -> mOnProductsLoadedListener.onProductsLoader(products));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

