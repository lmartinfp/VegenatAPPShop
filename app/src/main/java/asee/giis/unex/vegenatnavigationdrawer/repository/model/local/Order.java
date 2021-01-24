package asee.giis.unex.vegenatnavigationdrawer.repository.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders", indices = {@Index(value = {"id_order"}, unique = true)})
public class Order {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id_order")
    private long id_order;

    @ColumnInfo(name = "id_user")
    private long id_user;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "total_price")
    private float total_price;

    @ColumnInfo(name = "delivery_address")
    private String delivery_address;


    //Constructor parametrizado
    public Order(long id_user, String date, float total_price, String delivery_address) {
        this.id_user = id_user;
        this.date = date;
        this.total_price = total_price;
        this.delivery_address = delivery_address;
    }

    //Getters y setters
    public long getId_order() {
        return id_order;
    }

    public void setId_order(long id_order) {
        this.id_order = id_order;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }
}
