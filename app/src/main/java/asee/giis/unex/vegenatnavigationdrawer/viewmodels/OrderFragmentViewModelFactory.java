package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * Clase para instanciar el ViewModel de OrderFragment pasándole el repository
 **/
public class OrderFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private VegenatRepository vegenatRepository;

    public OrderFragmentViewModelFactory(VegenatRepository vegenatRepository) {
        this.vegenatRepository = vegenatRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new OrderFragmentViewModel(vegenatRepository);
    }
}
