package asee.giis.unex.vegenatnavigationdrawer.repository.model.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;

public class ProductNetworkDataSource {
    private static final String LOG_TAG = ProductNetworkDataSource.class.getSimpleName();
    private static ProductNetworkDataSource sInstance;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<Product[]> mDownloadedProducts;

    private ProductNetworkDataSource() {
        mDownloadedProducts = new MutableLiveData<>();
    }

    //Obtener la instancia
    public synchronized static ProductNetworkDataSource getInstance() {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            sInstance = new ProductNetworkDataSource();
            Log.d(LOG_TAG, "Made new network data source");
        }
        return sInstance;
    }

    //Método para obtener los productos de mDownloadedProducts (API)
    public LiveData<Product[]> getCurrentProducts() {
        return mDownloadedProducts;
    }

    /**
     * Cargamos en mDownloadedProducts los productos actuales de la API
     */
    public void fetchProducts() {
        Log.d(LOG_TAG, "Fetch products started");
        // Get gata from network and pass it to LiveData
        AppExecutors.getInstance().networkIO().execute(new ProductsNetworkLoaderRunnable(products -> mDownloadedProducts.postValue(products.toArray(new Product[0]))));
    }

}
