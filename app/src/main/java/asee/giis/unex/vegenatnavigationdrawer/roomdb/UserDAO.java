package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;

@Dao
public interface UserDAO {

    /**
     * Insertar un usuario y devolver su id autogenerado
     */
    @Insert
    long insertUser(User user);

    /**
     * Eliminar un usuario
     */
    @Query("DELETE FROM user WHERE id = :id")
    void deleteUser(long id);

    /**
     * Actualizar la info de un usuario
     */
    @Update
    int update(User user);

    /**
     * Devuelve un LiveData observando a un usuario según su id
     */
    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> searchUserById(long id);

    /**
     * Devuelve un usuario según su username
     */
    @Query("SELECT * FROM user WHERE username LIKE :name")
    User searchUserByName(String name);

    /**
     * Devuelve un usuario según su email
     */
    @Query("SELECT * FROM user WHERE email LIKE :email")
    User searchUserByEmail(String email);
}
