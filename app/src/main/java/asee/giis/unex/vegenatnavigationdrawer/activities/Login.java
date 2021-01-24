package asee.giis.unex.vegenatnavigationdrawer.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.LoginActivityViewModel;


/**
 * Activity para el Login
 */
public class Login extends AppCompatActivity {
    private EditText tvUsuario;
    private EditText tvContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvUsuario = findViewById(R.id.usernamesesion);
        tvContraseña = findViewById(R.id.passwordsesion);


        Button bIniciarSesion = findViewById(R.id.iniciarsesion);
        bIniciarSesion.setOnClickListener(view -> AppExecutors.getInstance().diskIO().execute(() -> {
            iniciarSesion(view);
        }));
    }

    /**
     * Método para iniciar sesión
     **/
    private void iniciarSesion (View view) {
        //Instanciamos el appContainer y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        LoginActivityViewModel mVM = new ViewModelProvider(this,
                appContainer.loginActivityViewModelFactory).get(LoginActivityViewModel.class);
        long res = mVM.checkSession(tvUsuario.getText().toString(), tvContraseña.getText().toString());
        appContainer.id_user = res; //asignamos el id del usuario
        appContainer.username = tvUsuario.getText().toString(); //metemos el usuario introducido
        runOnUiThread(() -> {
            if (res == -1) { //si devuelve -1 entonces el usuario no existe
                Snackbar.make(view, "El usuario " + tvUsuario.getText().toString() +
                        " no existe", Snackbar.LENGTH_LONG).
                        setAction("Action", null).show();
            }
            else {
                if (res == 0) { //si devuelve 0 es que existe pero la contraseña es incorrecta
                    Snackbar.make(view, "La contraseña no es correcta", Snackbar.LENGTH_LONG).
                            setAction("Action", null).show();
                }
                else { //si el login es correcto
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}