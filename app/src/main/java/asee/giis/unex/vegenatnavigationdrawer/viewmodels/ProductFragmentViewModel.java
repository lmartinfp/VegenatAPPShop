package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para el fragment de la lista de productos
 **/
public class ProductFragmentViewModel extends ViewModel {

    //Repositorio para los datos
    private final VegenatRepository vegenatRepository;
    //LiveData que observa la lista de productos
    private final LiveData<List<Product>> products;

    public ProductFragmentViewModel (VegenatRepository vegenatRepositoryP) {
        vegenatRepository = vegenatRepositoryP; //asignamos el repo porque va a ser único
        products = vegenatRepository.getCurrentProducts(); //Obtenemos los productos desde el repo
    }

    /**
     * Devuelve la lista de productos
     **/
    public LiveData <List<Product>> getProducts () {
        return products;
    }
}
