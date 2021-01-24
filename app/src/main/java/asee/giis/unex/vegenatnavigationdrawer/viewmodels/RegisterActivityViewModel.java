package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;


/**
 * ViewModel para la vista de registrar usuario
 **/
public class RegisterActivityViewModel extends ViewModel {

    //Repositorio para los datos
    private final VegenatRepository userRepository;

    public RegisterActivityViewModel (VegenatRepository userRepositoryP) {
        userRepository = userRepositoryP; //asignamos el repositorio
    }

    /**
     * Comprueba si tiene username único (sino devuelve -1), si tiene email único (devuelve 0) y si
     * se inserta bien devuelve el id
     **/
    public long insertUser (User user) {
        return userRepository.insertUser(user);
    }
}
