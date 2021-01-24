package asee.giis.unex.vegenatnavigationdrawer.repository.model.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//Tenemos que tener un índice con la columna username para poder acelerar las consultas en la base de datos y este debe ser único
@Entity(tableName = "user", indices = {@Index(value = {"id"}, unique = true)})

public class User {
    @PrimaryKey(autoGenerate = true) //Clave primaria autogenerada
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "username") //Columna de nombre de usuario
    private String username;
    @ColumnInfo(name = "email") //Columna de email
    private String email;
    @ColumnInfo(name = "password") //Columna de password
    private String password;
    @ColumnInfo(name = "address") //Columna de dirección
    private String address;

    //Constructor parametrizado
    public User (String username, String email, String password, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    //Métodos getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
