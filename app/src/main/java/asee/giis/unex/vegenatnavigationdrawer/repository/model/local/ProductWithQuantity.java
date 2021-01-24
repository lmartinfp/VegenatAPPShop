package asee.giis.unex.vegenatnavigationdrawer.repository.model.local;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "product_with_quantity")

public class ProductWithQuantity {

    @PrimaryKey (autoGenerate = true)
    private int idItemLista;

    @ColumnInfo(name = "id_user")
    private long id_user;

    @ColumnInfo(name = "product_name")
    private String product_name;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "price")
    private float price;

    //Constructor parametrizado
    public ProductWithQuantity(long id_user, String product_name, int quantity, float price) {
        this.id_user = id_user;
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
    }

    //Getters y setters
    public int getIdItemLista() {
        return idItemLista;
    }

    public void setIdItemLista(int idItemLista) {
        this.idItemLista = idItemLista;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
