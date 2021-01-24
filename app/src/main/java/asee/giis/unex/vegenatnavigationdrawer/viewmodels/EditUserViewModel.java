package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de edición de usuario
 **/
public class EditUserViewModel extends ViewModel {

    //Repositorio para el acceso a datos
    private final VegenatRepository userRepository;
    //LiveData para observar el usuario que se va a editar
    private final LiveData<User> user;


    public EditUserViewModel (VegenatRepository userRepositoryP) {
        userRepository = userRepositoryP; //asignamos el repo porque va a ser único
        user = userRepository.getUserById(); //Guardamos el LiveData que nos interesa observar que sera el usuario según el ID
    }

    /**
     * Metemos el id del usuario al mutableLiveData del repository
     **/
    public void setIdUser(final long id) {
        userRepository.setIdUser(id);
    }

    /**
     * Devolvemos el LiveData con el usuario observado
     **/
    public LiveData<User> getUserById() {
        return user;
    }

    /**
     * Editamos el usuario cuando demos al botón
     **/
    public int editUser (User user) {
        return userRepository.editUser(user);
    }


}
