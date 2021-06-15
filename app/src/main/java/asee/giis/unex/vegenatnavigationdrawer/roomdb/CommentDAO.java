package asee.giis.unex.vegenatnavigationdrawer.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Comment;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;

@Dao
public interface CommentDAO {

    /**
     * Insertar un comentario en la BD
     **/
    @Insert
    void insertComment(Comment comment);

    /**
     * Devolver una lista de comentarios en un observable (LiveData) en funcion del usuario
     **/
    @Query("SELECT * FROM comment WHERE id_user = :id_user")
    LiveData<List<Comment>> getCommentsByUser(long id_user);

    /**
     * Borrar los comentarios de un usuario
     **/
    @Query("DELETE FROM comment WHERE id_user = :id_user")
    void deleteCommentsByUser(long id_user);
}
