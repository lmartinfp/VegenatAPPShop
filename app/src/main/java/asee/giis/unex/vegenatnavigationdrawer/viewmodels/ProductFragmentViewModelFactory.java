package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link VegenatRepository}
 */
public class ProductFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final VegenatRepository mRepository;

    public ProductFragmentViewModelFactory(VegenatRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ProductFragmentViewModel(mRepository);
    }
}