package asee.giis.unex.vegenatnavigationdrawer.ui.products;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import asee.giis.unex.vegenatnavigationdrawer.AppContainer;
import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.MyApplication;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;
import asee.giis.unex.vegenatnavigationdrawer.viewmodels.ProductFragmentViewModel;

/**
 * A fragment representing a list of Items.
 */
public class ProductFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public SelectionListener mCallback; //Redefinición en los atributos de la clase
    private MyProductRecyclerViewAdapter mAdapter; //Adapter para la RecyclerView

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductFragment() {

    }

    //Customize parameter initialization
    public static ProductFragment newInstance(int columnCount) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.product_list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setVisibility(View.INVISIBLE);
            mAdapter = new MyProductRecyclerViewAdapter(DummyContent.ITEMS, this);
            recyclerView.setAdapter(mAdapter);

            //Obtenemos el contenedor de dependencias
            AppContainer appContainer = ((MyApplication) getActivity().getApplication()).appContainer;
            //Creamos el ViewModel aquí para que podamos controlar el ciclo de vida del ViewModel
            //Esto nos crea una instancia del viewmodel (con el repositorio) si no existe y si ya existe obtenemos la instancia creada con el factory
            ProductFragmentViewModel mVM = new ViewModelProvider(this, appContainer.factoryProductFragmentFactory).get(ProductFragmentViewModel.class);
            mVM.getProducts().observe(this.getViewLifecycleOwner(), products -> {
               mAdapter.swap(products);
                recyclerView.setVisibility(View.VISIBLE);
            });
        }
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (SelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SelectionListener");
        }
    }

    public interface SelectionListener{
        public void onListProductSelected(Product product);
    }

}