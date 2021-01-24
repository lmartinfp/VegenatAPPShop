package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * Clase para instanciar el ViewModel de RegisterActivity pasándole el repository
 **/
public class RegisterActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final VegenatRepository mRepository;

    public RegisterActivityViewModelFactory (VegenatRepository repository) {
        mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RegisterActivityViewModel(mRepository);
    }
}
