package asee.giis.unex.vegenatnavigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import asee.giis.unex.vegenatnavigationdrawer.activities.EditUser;
import asee.giis.unex.vegenatnavigationdrawer.activities.Login;
import asee.giis.unex.vegenatnavigationdrawer.activities.ProductDetailsActivity;
import asee.giis.unex.vegenatnavigationdrawer.activities.Register;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.ui.lista_compra.ProductShoppingDeleteListener;
import asee.giis.unex.vegenatnavigationdrawer.ui.products.ProductFragment;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements ProductFragment.SelectionListener, ProductShoppingDeleteListener {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int REQUEST_CODE_INICIAR_SESION = 76; //código de petición para el Activity de Login
    public static final int REQUEST_CODE_REGISTRAR= 77; //código de petición para el Activity de Register
    private Menu menu; //Menú del ActionBar
    private NavigationView navigationView;
    private AppContainer appContainer;
    private Toolbar toolbar;
    private TextView textView;
    private MainActivityViewModel mVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos la toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Obtenemos el navigationdrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //Sin haber iniciado sesión al comeinzo del inicio de la app, ocultamos los items
        sesionNavDrawer(false);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appContainer = ((MyApplication) getApplication()).appContainer;

        //AÑADIR LOS FRAGMENTOS QUE SE NECESITEN
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_productos, R.id.nav_home, R.id.nav_lista_compra, R.id.nav_pedidos, R.id.nav_preguntasfrecuentes)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mVM = new ViewModelProvider(this, appContainer.mainActivityViewModelFactory).get(MainActivityViewModel.class);
    }

    /**
     * Método de la interfaz SelectionListener
     **/
    @Override
    public void onListProductSelected(Product item) {
        //Le paso los parámetros para que vaya al item que nosotros queramos
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        Intent i = new Intent(MainActivity.this, ProductDetailsActivity.class);
        i.putExtras(bundle);
        startActivity(i);
   }

    @Override
    public void onProductShoppingDelete(ProductWithQuantity productWithQuantity) {
        AppExecutors.getInstance().diskIO().execute(() -> mVM.deleteProductShoppingItem(productWithQuantity));
    }

    /**
     * Este método (creación del menú del Appbar) se ejecuta al principio cuando se crea la MainActivity
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu; //Cuando se inicializa el menú lo guardamos en la variable de la clase
        sesionUserMenu(true, false);
        textView = findViewById(R.id.user_nav_drawer);
        textView.setText("No has iniciado sesión");
        return true;
    }

    /**
     * Para las opciones del ActionBar (toolbar) se llama a este método
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //Según el id del item del menú
            case R.id.login_action: //Si elegimos el boton de login (icono de avatar de usuario)
                Intent iniciar = new Intent(this, Login.class); //Creamos el Intent a la activity Login
                startActivityForResult(iniciar, REQUEST_CODE_INICIAR_SESION); //Iniciamos la actividad esperando resultado el login
                return true; //Devolvemos true para salir

            case R.id.register_action: //Si elegimos el botón de registrar
                Intent registrar = new Intent(this, Register.class); //Intent a register
                startActivityForResult(registrar, REQUEST_CODE_REGISTRAR); //Iniciamos la actividad esperando resultado el registro
                return true; //Devolvemos true para salir

            case R.id.borrar_usuario:
                MainActivityViewModel mVM = new ViewModelProvider(this, appContainer.mainActivityViewModelFactory).get(MainActivityViewModel.class);
                AppExecutors.getInstance().diskIO().execute(() -> {
                    mVM.deleteUser(appContainer.username, appContainer.id_user);
                    runOnUiThread(() -> {
                        closeSession();

                        //Sin haber iniciado sesión al comeinzo del inicio de la app, ocultamos los items
                        sesionNavDrawer(false);
                        sesionUserMenu(true, false);
                    });
                });
                return true;

            case R.id.cerrar_sesion: //Si elegimos el botón de cerrar sesión
                closeSession();

                //Sin haber iniciado sesión al comeinzo del inicio de la app, ocultamos los items
                sesionNavDrawer(false);
                sesionUserMenu(true, false);
                return true;

            case R.id.editar_usuario://Si se quiere editar usuario
                Intent editar = new Intent(this, EditUser.class); //Creamos el Intent a la activity Login
                startActivityForResult(editar, REQUEST_CODE_INICIAR_SESION); //Iniciamos la actividad esperando resultado el login

            default:
                //Si la acción no coincide con ninguno de los ítems (se pulsará el botón Up)
                //entonces devolvemos el control a la actividad padre (en este caso no habrá botón up puesto que es la actividad principal)
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     *  Método que devuelve el control de la segunda Activity (Login o Register)
     *  ReqCode: código de petición que le paso a la segunda activity
     *  ResCode: codigo de respuesta que me devuelve la segunda activity
     *  Intent data: datos de vuelta (puede ser un bundle vacío o puede devolverme el nombre de usuario si se inició sesión
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //data es returnIntent en Login y Register
        super.onActivityResult(requestCode, resultCode, data);

        //Cuando finaliza el LoginActivity o RegisterActivity
        if ((REQUEST_CODE_INICIAR_SESION == requestCode || REQUEST_CODE_REGISTRAR == requestCode) && resultCode == RESULT_OK) { //Comprobamos que el código de petición es el mismo que envié y que el resultado es OK
            sesionUserMenu(false, true);
            textView.setText("Sesión iniciada\nUsuario logeado: " + appContainer.username);
        }

        //Comprobamos si hay un usuario distinto de nulo y si no está vacío
        if (appContainer.username != null && !appContainer.username.isEmpty()) {
            sesionNavDrawer(true);
        }
    }

    /**
     * Ocultar y mostrar ítems del menú de usuario en función de b1 y b2
     **/
    public void sesionUserMenu(boolean b1, boolean b2) {
        menu.findItem(R.id.login_action).setVisible(b1);
        menu.findItem(R.id.register_action).setVisible(b1);
        menu.findItem(R.id.cerrar_sesion).setVisible(b2);
        menu.findItem(R.id.editar_usuario).setVisible(b2);
        menu.findItem(R.id.borrar_usuario).setVisible(b2);
    }

    /**
     * Ocultar y mostrar ítems del navigationdreawer en función de b1
     **/
    public void sesionNavDrawer (boolean b1) {
        navigationView.getMenu().findItem(R.id.nav_home).setVisible(b1);
        navigationView.getMenu().findItem(R.id.nav_lista_compra).setVisible(b1);
        navigationView.getMenu().findItem(R.id.nav_pedidos).setVisible(b1);
    }

    /**
     * Método para ir al fragmento de productos después de cerrar sesión o borrar usuario
     * (no se puede hacer con un replace porque se solapan los fragments)
     **/
    public void closeSession() {
        appContainer.username = null;
        appContainer.id_user = -1;
        textView.setText("No has iniciado sesión");
        //Quitamose el fragment de la pila
        getSupportFragmentManager().popBackStack();

        //Vamos a productos
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        toolbar.setTitle("Productos");
        menuItem.setChecked(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}