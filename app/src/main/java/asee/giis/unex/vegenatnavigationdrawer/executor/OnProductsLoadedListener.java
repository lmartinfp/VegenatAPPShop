package asee.giis.unex.vegenatnavigationdrawer.executor;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;

/**
 * Interfaz para cargar los productos
 **/
public interface OnProductsLoadedListener {

    /**
     * Método que carga los productos
     **/
    void onProductsLoader(List<Product> products);
}
