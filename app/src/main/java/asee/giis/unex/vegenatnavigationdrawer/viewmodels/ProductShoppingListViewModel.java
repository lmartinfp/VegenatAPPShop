package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de la lista de la compra
 **/
public class ProductShoppingListViewModel extends ViewModel {
    //Repositorio para los datos
    private final VegenatRepository productShoppingListRepository;
    //LiveData que contiene la dista de productos con la cantidad que ha guardado el usuario en su lista de la coompra
    private final LiveData<List<ProductWithQuantity>> productsWithQuantity;

    public ProductShoppingListViewModel (VegenatRepository productShoppingListRepositoryP) {
        productShoppingListRepository = productShoppingListRepositoryP; //asignamos el repo porque va a ser único
        productsWithQuantity = productShoppingListRepository.getShoppingListByUser(); //obtenemos la lista de la compra del usuario
    }

    /**
     * Obtenemos la lista de la compra del usuario en un LiveData
     **/
    public LiveData<List<ProductWithQuantity>> getShoppingListByUser () {
        return productsWithQuantity;
    }

    /**
     * Guardamos el id del usuario para filtrarlo en el LiveData de la lista de la compra
     **/
    public void setIdFilterByUser (long id) {
        productShoppingListRepository.setIdShoppingList(id);
    }

    /**
     * Borramos todos los productos de la lista de la compra de un usuario
     **/
    public void deleteShoppingListByUser(long id_user) {
        productShoppingListRepository.deleteProductShoppingListByUser(id_user);
    }

    /**
     * Insertamos un pedido
     **/
    public void insertOrder (long id_user, String username) {
        productShoppingListRepository.insertOrder(id_user, username);
    }

}
