package asee.giis.unex.vegenatnavigationdrawer.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.EditUserViewModel;

public class EditUser extends AppCompatActivity {

    private TextView username;
    private EditText email, address, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        //Obtenemos la actionbar
        ActionBar ab = getSupportActionBar();

        //Activamos el botón up
        ab.setDisplayHomeAsUpEnabled(true);

        //Obtenemos los elementos de la interfaz
        username = findViewById(R.id.editusername);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddress);
        password = findViewById(R.id.editpassword);

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        EditUserViewModel mVM = new ViewModelProvider(this, appContainer.editUserViewModelFactory).get(EditUserViewModel.class);

        //Obtenemos el id y modificamos el MutableLiveData
        long id = appContainer.id_user;
        mVM.setIdUser(id);

        //Observar los cambios del perfil
        mVM.getUserById().observe(this, user -> {
            username.setText(user.getUsername());
            email.setText(user.getEmail());
            address.setText(user.getAddress());
            password.setText("Contraseña");
        });

        //Botón y onClick
        Button bSaveChanges = findViewById(R.id.savechanges);
        bSaveChanges.setOnClickListener(v -> {
            if (password.getText().toString().isEmpty()) {
                Snackbar.make(v, "Introduce la contraseña para poder salvar los cambios", Snackbar.LENGTH_LONG).
                              setAction("Action", null).show();
            }
            else {
                if (address.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Snackbar.make(v, "No puede haber campos vacíos", Snackbar.LENGTH_LONG).
                            setAction("Action", null).show();
                }
                else {
                  editUser(v, mVM, id);
                }
            }
        });
    }

    //Método que comprueba el proceso de edición de datos del usuario
    private void editUser (View v, EditUserViewModel mVM, long id) {
        //Hilo secundario para acceso a datos
        AppExecutors.getInstance().diskIO().execute(() -> {
            User user = new User (username.getText().toString(), email.getText().toString(),
                    password.getText().toString(), address.getText().toString());
            user.setId(id);
            int cambio = mVM.editUser(user);
            //Hilo principal para pintar una barra emergente con el resultado
            runOnUiThread(() -> {
                switch (cambio) {
                    case -2:
                        Snackbar.make(v, "La contraseña no es correcta", Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                    case -1:
                        Snackbar.make(v, "No ha habido cambios", Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                    case 0:
                        Snackbar.make(v, "El email está en uso", Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                    case 1:
                        Snackbar.make(v, "El email no es válido: debe tener entre 8 y 16 caracteres, una mayúscula y una minúscula", Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                    case 2:
                        Snackbar.make(v, "Proceso completado: Se han cambiado los datos", Snackbar.LENGTH_LONG).
                                setAction("Action", null).show();
                        break;
                }
            });
        });
    }

}
