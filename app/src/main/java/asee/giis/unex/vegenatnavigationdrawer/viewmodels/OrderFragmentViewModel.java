package asee.giis.unex.vegenatnavigationdrawer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;
import asee.giis.unex.vegenatnavigationdrawer.repository.VegenatRepository;

/**
 * ViewModel para la vista de la lista de pedidos
 **/
public class OrderFragmentViewModel extends ViewModel {

    //Repositorio para el acceso a datos
    private final VegenatRepository vegenatRepository;
    //LiveData observando a la lista de pedidos
    private LiveData<List<Order>> ordersLiveData;

    public OrderFragmentViewModel (VegenatRepository vegenatRepositoryP) {
        vegenatRepository = vegenatRepositoryP; //asignamos el repo porque va a ser único
        ordersLiveData = vegenatRepository.getOrdersByUser(); //obtenemos el livedata con la lista de pedidos del usuario

    }

    /**
     * Asignamos al MutableLiveData para filtrar la lista el id del usuario
     **/
    public void setIdFilterOrder (long id) {
        vegenatRepository.setIdFilterOrders(id);
    }

    /**
     * Recuperamos el liveData con la lista de usuarios para observarla
     **/
    public LiveData<List<Order>> getOrdersByUser () {
        return ordersLiveData;
    }
}
