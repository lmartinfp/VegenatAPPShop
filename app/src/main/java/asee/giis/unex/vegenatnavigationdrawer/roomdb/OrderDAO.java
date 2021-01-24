package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;

@Dao
public interface OrderDAO {

    /**
     * Insertar un pedido en la BD
     **/
    @Insert
    void insertOrder (Order order);

    /**
     * Devolver una lista de pedidos en un observable (LiveData)
     **/
    @Query("SELECT * FROM orders WHERE id_user = :id_user")
    LiveData<List<Order>> getOrdersByUser (long id_user);

    /**
     * Borrar los pedidos de un usuario
     **/
    @Query("DELETE FROM orders WHERE id_user = :id_user")
    void deleteOrdersByUser (long id_user);
}
