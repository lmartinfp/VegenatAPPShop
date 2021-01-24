package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;

import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de login
 **/
public class LoginActivityViewModel extends ViewModel {

    //Repositorio para el acceso a datos
    private final VegenatRepository userRepository;

    public LoginActivityViewModel (VegenatRepository userRepositoryP) {
        userRepository = userRepositoryP; //asignamos el repo porque va a ser único
    }

    /**
     * Chequeo de sesión: pasamos el username y la password y en función de lo que devuelva
     * asignamos el id si se inicia sesión correctamente o mostramos un snackbar en el activity
     * diciendo por qué no ha podido iniciar sesión
     **/
    public long checkSession (String username, String password) {
        return userRepository.checkSession(username, password);
    }
}
