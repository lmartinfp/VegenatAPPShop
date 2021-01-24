package asee.giis.unex.vegenatnavigationdrawer.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.User;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.RegisterActivityViewModel;

/**
 * Activity para registrar un usuario
 */
public class Register extends AppCompatActivity {

    //Campos del formulario
    private EditText username, password, email, confirmpassword, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Obtenemos la actionbar
        ActionBar ab = getSupportActionBar();
        //Activamos el botón up
        ab.setDisplayHomeAsUpEnabled(true);

        //Obtenemos los campos del formulario
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        confirmpassword = findViewById(R.id.confirmpassword);
        address = findViewById(R.id.address);

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        RegisterActivityViewModel mVM = new ViewModelProvider(this, appContainer.registerActivityViewModelFactory).get(RegisterActivityViewModel.class);

        Button bRegister = findViewById(R.id.registrarse);

        //El paso de parámetros tienen que ir dentro del ClickListener pq es cuando le doy al botón
        bRegister.setOnClickListener(view -> {
            //Hacemos una serie de comprobaciones (si algún parámetro (excepto dirección) del formulario es nulo, si las contraseñas no coinciden,
            //El resto de comprobaciones con la BD las hacemos en el repositorio y devolvemos un int en función del resultado

            //Guardamos el valor del campo en un String para mayor comodidad
            String usernameString = username.getText().toString();
            String passwordString = password.getText().toString();
            String emailString = email.getText().toString();
            String confirmpasswordString = confirmpassword.getText().toString();
            String addressString = address.getText().toString();

            //Comprobamos si los campos del formulario están vacíos o no y si las contraseñas coinciden
            boolean correct = checkForm(view, usernameString, passwordString, confirmpasswordString, emailString, addressString);
            if (correct) {
                //Registramos
                register(view, usernameString, emailString, passwordString, addressString, mVM, appContainer);
            }

        });
    }



    /**
     * Método que checkea el formulario (si no hay ningún campo vacío excepto la dirección y coinciden las contraseñas)
     **/
    private boolean checkForm (View view, String username, String password, String confirmpassword, String email, String address) {
        boolean correct = false;
        if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Snackbar.make(view, "No puede haber ningún campo vacío", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            if (!password.equals(confirmpassword)) {
                Snackbar.make(view, "Las contraseñas introducidas no coinciden", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                correct = true;
            }
        }
        return correct;
    }

    /**
     * Método que checkea el formulario (si no hay ningún campo vacío excepto la dirección y coinciden las contraseñas)
     **/
    private void register (View view, String username, String email, String password, String address, RegisterActivityViewModel mVM, AppContainer appContainer) {
        User user = new User(username, email, password, address);
        AppExecutors.getInstance().diskIO().execute(() -> {
            long res = mVM.insertUser(user); //intentamos insertar el usuario y devolverá un mensaje

            //la interacción de la IU debo hacerla en el hilo principal
            runOnUiThread(() -> {
                if (res == -3) {
                    Snackbar.make(view, "Elige una contraseña entre 8 y 16 caracteres, con un número, minúscula y mayúscula", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    if (res == -2) {
                        Snackbar.make(view, "No has introducido un email válido", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        if (res == -1) {
                            Snackbar.make(view, "El nombre de usuario está en uso", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } else {
                            if (res == 0) {
                                Snackbar.make(view, "El email está en uso", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            } else { //si ha ido correcto
                                //Guardo el id y username
                                appContainer.username = username;
                                appContainer.id_user = res;
                                //Devuelvo el control al MainActivity
                                Intent returnIntent = new Intent();
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            }
                        }
                    }
                }
            });
        });
    }
}
