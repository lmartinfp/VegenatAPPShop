package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDAO {

    /**
     * Añadir todos los productos y en caso de haber conflicto se reemplazan
     **/
    @Insert(onConflict = REPLACE)
    void bulkInsert(List<Product> products);


    /**
     * Devolver en un LiveData la lista de todos los productos para observarla
     **/
    @Query("SELECT * FROM product")
    LiveData<List<Product>> listAllProducts();

    /**
     * Devolver en un LiveData un producto buscado por su id
     **/
    @Query("SELECT * FROM product WHERE id LIKE :id")
    LiveData<Product> searchById(int id);

    /**
     * Borrar todos los productos de la BD
     **/
    @Query("DELETE FROM product")
    int deleteAllProducts();

}
