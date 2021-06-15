package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Comment;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;

/**
 * Base de datos de Room
 */
@Database(entities = {User.class, ProductWithQuantity.class, Product.class, Order.class, Comment.class}, version = 1)
public abstract class VegenatDatabase extends RoomDatabase {
    private static VegenatDatabase instance;

    /**
     * Obtener la instancia de la BD (Singleton)
     */
    public static VegenatDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, VegenatDatabase.class, "onlineshopvegenat2.db").build();
        return instance;
    }

    /**
     * Obtener el DAO de usuarios
     */
    public abstract UserDAO getUserDAO();

    /**
     * Obtener el DAO de productShoppingList
     */
    public abstract ProductShoppingListDAO getProductShoppingListDAO();

    /**
     * Obtener el DAO de productos
     */
    public abstract ProductDAO getProductDAO();

    /**
     * Obtener el DAO de pedidos
     */
    public abstract OrderDAO getOrderDAO();

    /**
     * Obtener el DAO de comentarios
     */
    public abstract CommentDAO getCommentDAO();

}
