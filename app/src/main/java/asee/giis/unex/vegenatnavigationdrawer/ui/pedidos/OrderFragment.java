package asee.giis.unex.vegenatnavigationdrawer.ui.pedidos;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.OrderFragmentViewModel;

/**
 * A fragment representing a list of Items.
 */
public class OrderFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyOrderRecyclerViewAdapter mAdapter;
    private TextView listaVacia;
    private ImageView empty_list_image;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderFragment() {

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
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        // Set the adapter
        //Si es una instancia de LinearLayour (elemento padre)
        if (view instanceof LinearLayout) {
            Context context = view.getContext();

            //Obtenemos la recycler view
            recyclerView = view.findViewById(R.id.order_list);

            //Si el número de columnas de la recyclerView es 1 entonces creamos un LinearLayout
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else { //Si el número de columnas de la recyclerView es mayor que 1 entonces creamos un layout de rejilla
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //Hacemos invisible la lista al crear el fragment y ponemos el adapter en principio vacío
            recyclerView.setVisibility(View.INVISIBLE);
            mAdapter = new MyOrderRecyclerViewAdapter(DummyContent.ITEMS);
            recyclerView.setAdapter(mAdapter);

            listaVacia = view.findViewById(R.id.lista_vacia2);
            empty_list_image = view.findViewById(R.id.empty_list_image2);

            //Obtenemos el contenedor de dependencias
            AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
            //Creamos el ViewModel aquí para que podamos controlar el ciclo de vida del ViewModel
            OrderFragmentViewModel mVM = new ViewModelProvider(this,
                    appContainer.orderFragmentViewModelFactory).get(OrderFragmentViewModel.class);

            //Primero se llama al setId para hacer setValue al MutableLiveData del Repository y que cuando se llame
            //a getOrdersByUser tenga un MutableLiveData por el que filtrar ya que necesita un parámetro que es
            //el id del usuario
            long id_user = appContainer.id_user;
            mVM.setIdFilterOrder(id_user);

            //El observe se hace en un hilo secundario -> no hace falta hacer un Runnable
            mVM.getOrdersByUser().observe(getViewLifecycleOwner(), orders -> {
                getOrdersByUser(orders);
            });
        }
        return view;
    }

    /**
     * Gestiona el observer de la lista de pedidos de un usuario
     **/
    private void getOrdersByUser (List<Order> orders) {
        //Intercambiamos la lista y hacemos visible la recyclerview
        mAdapter.swap(orders);
        recyclerView.setVisibility(View.VISIBLE);
        if (orders.size() == 0) {
            listaVacia.setTextSize(18f);
            listaVacia.setGravity(Gravity.CENTER);
            listaVacia.setVisibility(View.VISIBLE);
            empty_list_image.setVisibility(View.VISIBLE);
        }
        else {
            listaVacia.setTextSize(0f);
            listaVacia.setVisibility(View.INVISIBLE);
            empty_list_image.setVisibility(View.INVISIBLE);
        }
    }
}