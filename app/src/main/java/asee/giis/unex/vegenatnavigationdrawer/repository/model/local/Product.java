
package asee.giis.unex.vegenatnavigationdrawer.repository.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Tenemos que tener un índice con la columna username para poder acelerar las consultas en la base de datos y este debe ser único
@Entity(tableName = "product", indices = {@Index(name = "search_product", value = {"id", "title"}, unique = true)})
public class Product {

    //SerializedName y Expose hacen referencia a los campos de los datos de la API
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title") //Columna de título de producto
    private String title;
    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description") //Columna de descripción de producto
    private String description;
    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price") //Columna de título de producto
    private Float price;
    @SerializedName("imagelink")
    @Expose
    @ColumnInfo(name = "imagelink") //Columna de link de la imagen del producto
    private String imagelink;

    //Constructor parametrizado creando objetos con datos obtenidos de la API
    public Product(int id, String title, String description, Float price, String imagelink) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imagelink = imagelink;
    }

    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

}
