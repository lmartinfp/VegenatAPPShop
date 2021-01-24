package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


;import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;

@Dao
public interface ProductShoppingListDAO {
    /**
     * Insertar un producto en la lista de la compra
     **/
    @Insert
    void insertProductShoppingList(ProductWithQuantity productWithQuantity);

    /**
     * Eliminar un producto en la lista de la compra
     **/
    @Delete
    void deleteProductShoppingList(ProductWithQuantity productWithQuantity);

    /**
     * Devolver en un LiveData la lista de todos los productos de la lista de la compra de un usuario
     **/
    @Query("SELECT * FROM product_with_quantity WHERE id_user = :id_user")
    LiveData<List<ProductWithQuantity>> recoverAllProductsShoppingList(long id_user);

    /**
     * Devolver en un LiveData todos los productos de la lista de la compra para hacer el pedido
     **/
    @Query("SELECT * FROM product_with_quantity WHERE id_user = :id_user")
    List<ProductWithQuantity> recoverAllProductsToOrder(long id_user);

    /**
     * Borrar todos los productos de la lista de la compra de un usuario
     */
    @Query("DELETE FROM product_with_quantity WHERE id_user = :id_user")
    void deleteAllProductsShoppingList(long id_user);

}
