package asee.giis.unex.vegenatnavigationdrawer.api;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductInterfaceAPI {

    /**
     * Método que hace una petición de GET y devuelve en una llamada la lista de productos al path
     * especificado de la API
     **/
    @GET("cristianmj99/vpn2/main/products.json")
    Call<List<Product>> listProducts();
}