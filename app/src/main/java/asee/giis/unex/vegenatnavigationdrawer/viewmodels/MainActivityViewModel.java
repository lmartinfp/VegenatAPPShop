package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de MainActivity
 **/
public class MainActivityViewModel extends ViewModel {

    //Repositorio para el acceso a datos
    private final VegenatRepository vegenatRepository;

    public MainActivityViewModel (VegenatRepository userRepositoryP) {
        vegenatRepository = userRepositoryP; //asignamos el repo porque va a ser único
    }

    public void deleteUser (String username, long id) {
        vegenatRepository.deleteUser(username, id);
    }

    public void deleteProductShoppingItem (ProductWithQuantity productWithQuantity) {
        vegenatRepository.deleteProductShoppingList(productWithQuantity);
    }
}
