package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Comment;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de detalles de un producto
 **/
public class ProductDetailsActivityViewModel extends ViewModel {

    //Repositorio para acceder a los datos del producto
    private final VegenatRepository vegenatRepository;

    //LiveData para observar el producto mostrado
    private LiveData<Product> productLiveData;

    public ProductDetailsActivityViewModel (VegenatRepository vegenatRepositoryP) {
        vegenatRepository = vegenatRepositoryP; //asignamos el repo porque va a ser único
        productLiveData = vegenatRepository.getProductById(); //obtenemos el producto por el id
    }

    /**
     * Metemos el id del producto al mutableLiveData del repository
     **/
    public void setIdProduct (final int id_product) {
        vegenatRepository.setIdFilterProduct(id_product);
    }

    /**
     * Recuperamos el LiveData del producto con su id para observarlo
     **/
    public LiveData<Product> getProductById() {
        return productLiveData;
    }

    /**
     * Insertamos un producto con la cantidad elegida en la lista de la compra
     **/
    public void insertProductShoppingList(ProductWithQuantity productWithQuantity) {
        vegenatRepository.addProductSoppingList(productWithQuantity);
    }

    /**
     * Insertamos un comentario con su texto y su puntuacion
     **/
    public void insertComment(Comment comment) {
        vegenatRepository.insertComment(comment);
    }

}
