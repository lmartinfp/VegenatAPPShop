package asee.giis.unex.vegenatnavigationdrawer.repository.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment", indices = {@Index(value = {"id_product", "id_user"}, unique = true)})
public class Comment {

    @PrimaryKey
    @ColumnInfo(name = "id_product")
    private long id_product;


    @ColumnInfo(name = "id_user")
    private long id_user;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "score")
    private int score;


    //Constructor parametrizado
    public Comment(long id_product, long id_user, String text, int score) {
        this.id_product = id_product;
        this.id_user = id_user;
        this.text = text;
        this.score = score;

    }

    //Getters y setters
    public long getId_product() {
        return id_product;
    }

    public void setId_product(long id_product) {
        this.id_product = id_product;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
