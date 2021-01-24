package asee.giis.unex.vegenatnavigationdrawer.ui.lista_compra;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.executor.AppExecutors;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductShoppingListViewModel;

/**
 * A fragment representing a list of ProductWithQuantity.
 */
public class ProductShoppingListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyProductShoppingListRecyclerViewAdapter mAdapter;

    //TextView para mostrarlo cuando la lista está vacía, imagen de lista vacía, botón de realizar pedido y RecyclerView
    private TextView lista_vacia;
    private Button realizarPedido, vaciarLista;
    private RecyclerView recyclerView;
    private ImageView empty_list_image;

    //ViewModel
    private ProductShoppingListViewModel mVM;

    //Interfaz para borrar un producto de la lista
    public ProductShoppingDeleteListener mCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductShoppingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_shopping_list, container, false);

        // Set the adapter
        //Obtenemos una instancia de ScrollView que es el elemento padre del layour
        if (view instanceof ScrollView) {
            Context context = view.getContext();

            //Localizamos a partir del elemento padre la RecyclerView
            recyclerView = view.findViewById(R.id.listShopping);
            if (mColumnCount <= 1) { //Si el número de columnas es 1 entonces crea un LinearLayout
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else { //Si el número de columnas es más de 1 entonces crea una rejilla
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //Hacemos invisible la lista al crear el fragment y ponemos el adapter en principio vacío
            recyclerView.setVisibility(View.INVISIBLE);
            mAdapter = new MyProductShoppingListRecyclerViewAdapter(DummyContent.ITEMS, this);
            recyclerView.setAdapter(mAdapter);

            //Obtenemos el contenedor de dependencias
            AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
            //Creamos el ViewModel aquí para que podamos controlar el ciclo de vida del ViewModel
            mVM = new ViewModelProvider(this,
                    appContainer.productShoppingListViewModelFactory).get(ProductShoppingListViewModel.class);


            //Localizamos el botón de realizar pedido y el de mostrar la lista vacía
            lista_vacia = view.findViewById(R.id.lista_vacia);
            empty_list_image = view.findViewById(R.id.empty_list_image);
            realizarPedido = view.findViewById(R.id.realizarpedido);
            vaciarLista = view.findViewById(R.id.vaciar_lista_compra);

            //Actualizamos el MutableLiveData con el id de usuario para mostrar la lista en función de su id
            mVM.setIdFilterByUser(appContainer.id_user);

            //En Fragment se llama al ciclo de vida de la vista que es dueño del fragment (la activity)
            //Observamos la lista
            mVM.getShoppingListByUser().observe(getViewLifecycleOwner(), productWithQuantities -> {
                accionesLista(productWithQuantities);
            });

            //Inserar pedido cuando se dé al botón
            realizarPedido.setOnClickListener(v -> {
                insertarPedidoYBorrarLista(mVM, appContainer, v);
            });

            vaciarLista.setOnClickListener(v -> vaciarLista(v, appContainer));

        }
        return view;
    }

    private void accionesLista (List<ProductWithQuantity> productWithQuantities) {
        mAdapter.swap(productWithQuantities); //Intercambiamos la lista en el adapter
        recyclerView.setVisibility(View.VISIBLE); //CUando se ha puesto la lista de productos hacemos visible la recycler view
        if (productWithQuantities.size() == 0) { //Si la lista está vacía
            //Mostramos un texto de lista vacía, la imagen y ocultamos el botón de realizar pedido
            lista_vacia.setTextSize(18f);
            lista_vacia.setGravity(Gravity.CENTER);
            lista_vacia.setVisibility(View.VISIBLE);
            empty_list_image.setVisibility(View.VISIBLE);
            realizarPedido.setVisibility(View.INVISIBLE);
            vaciarLista.setVisibility(View.INVISIBLE);
        }
        else { //Si tiene algún elemento lo mostramos
            realizarPedido.setVisibility(View.VISIBLE);
            vaciarLista.setVisibility(View.VISIBLE);
            lista_vacia.setTextSize(0f);
            lista_vacia.setVisibility(View.INVISIBLE);
            empty_list_image.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Método que inserta un pedido con los productos de la lista de la comrpa y luego la borra
     */
    private void insertarPedidoYBorrarLista(ProductShoppingListViewModel mVM, AppContainer appContainer, View view) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            //Insertamos pedido con el id de usuario y borramos su lista de la compra mostrando, además, un mensaje de confirmación
            mVM.insertOrder(appContainer.id_user, appContainer.username);
            mVM.deleteShoppingListByUser(appContainer.id_user);
            getActivity().runOnUiThread(() -> Snackbar.make(view, "El pedido se ha realizado correctamente", Snackbar.LENGTH_LONG).setAction("Action", null).show());
        });
    }

    /**
     * Vaiar lista de la compra
     **/
    private void vaciarLista (View view, AppContainer appContainer) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            //Insertamos pedido con el id de usuario y borramos su lista de la compra mostrando, además, un mensaje de confirmación
            mVM.deleteShoppingListByUser(appContainer.id_user);
            getActivity().runOnUiThread(() -> Snackbar.make(view, "Se ha vaciado la lista de la compra", Snackbar.LENGTH_LONG).setAction("Action", null).show());
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (ProductShoppingDeleteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ProductShoppingDeleteListener");
        }
    }
}