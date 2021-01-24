 package asee.giis.unex.vegenatnavigationdrawer.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.glide_adapter.ImageAdapter;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductDetailsActivityViewModel;


public class ProductDetailsActivity extends AppCompatActivity {

    private TextView titleTV, descriptionTV, priceTV;
    private ImageView imagen;
    private String username;
    private EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Obtenemos la toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);

        //Le ponemos el título
        getSupportActionBar().setTitle("Información del producto");

        //Obtenemos la actionbar
        ActionBar ab = getSupportActionBar();
        //Activamos el botón up
        ab.setDisplayHomeAsUpEnabled(true);

        //Obtenemos los elementos de la interfaz
        titleTV = findViewById(R.id.tvtitle);
        descriptionTV = findViewById(R.id.tvdescription);
        imagen = findViewById(R.id.imageLink);
        priceTV = findViewById(R.id.tvprice);

        //Obtenemos en un objeto bundle los argumentos que le hemos pasado desde ProducDetailsActivity (un bundle entero)
        Bundle bundle = getIntent().getExtras();

        //Obtenemos el id del bundle
        int id = bundle.getInt("id");

        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        ProductDetailsActivityViewModel mVM = new ViewModelProvider(this,
                                        appContainer.factoryProductDetailsActivityViewModelFactory).get(ProductDetailsActivityViewModel.class);

        mVM.setIdProduct(id);
        mVM.getProductById().observe(this, product -> {
            //Cargamos los textos de título, descripción y precio
            titleTV.setText(product.getTitle());
            descriptionTV.setText(product.getDescription());
            priceTV.setText(product.getPrice().toString());

            //Cargamos la URL de la imagen con Glide
            ImageAdapter.loadUrl(this, product.getImagelink(), imagen);
        });

        //Obtenemos la vista de las undiades
        quantity = findViewById(R.id.quantity);

        //Obtenemos usuario
        String username = ((MyApplication) getApplication()).appContainer.username;


        //Obtenemos el botón de comprar
        Button bAccion = findViewById(R.id.accion);
        if (username == null) { //Compruebo si no hay ningún bundle (no sesión de usuario)
            //Ocultamos botón y texto para elegir la cantidad
            LinearLayout layoutCantidad = findViewById(R.id.layoutCantidad);
            layoutCantidad.setVisibility(View.INVISIBLE);
        }
        else { //Compruebo si hay una usuario metido en el bundle (sesión de usuario iniciada)
            //Si se pulsa el botón de comprar
            bAccion.setOnClickListener(v -> {
                insertarElementoEnListaCompra(mVM, v, appContainer);
            });
        }
    }

    /**
     * Método que inserta un elemento en la lista de la compra haciendo una serie de comprobaciones
     **/
    private void insertarElementoEnListaCompra (ProductDetailsActivityViewModel mVM, View v, AppContainer appContainer) {
        if (quantity.getText().toString().isEmpty() || Integer.parseInt(quantity.getText().toString()) == 0) { //Si el cuadro de texto de las unidades está vacío
            Snackbar.make(v, "Introduce un número de unidades y que sea mayor que 0", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            int unidades = Integer.parseInt(quantity.getText().toString()); //Obtenemos las unidades que ha introducido el usuario
            //Insertamos en el Repository a través del VM la fila del producto en la base de datos
            long id_user = appContainer.id_user;
            ProductWithQuantity productWithQuantity = new ProductWithQuantity(id_user, titleTV.getText().toString(),
                    unidades, Float.parseFloat(priceTV.getText().toString()));
            AppExecutors.getInstance().diskIO().execute(() ->
                    mVM.insertProductShoppingList(productWithQuantity));
            runOnUiThread(() -> {
                ProductDetailsActivity.this.finish();
                Toast.makeText(this, "El producto se ha añadido correctamente", Toast.LENGTH_LONG).show();
            });
        }
    }

    /**
     * Método para el menú de la ActionBar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
