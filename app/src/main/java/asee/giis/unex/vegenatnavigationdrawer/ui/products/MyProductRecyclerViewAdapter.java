package asee.giis.unex.vegenatnavigationdrawer.ui.products;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Product}.
 */
public class MyProductRecyclerViewAdapter extends RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder> {

    private List<Product> mValues = new ArrayList<Product>(25);

    //Se pasa el fragmento de la recyclerview para poder hacer un callback hacia la activity cuando se seleccione un producto de la lista
    private ProductFragment parentFragment;

    public MyProductRecyclerViewAdapter(List<Product> items, ProductFragment parentFragmentP) {
        mValues = items;
        parentFragment = parentFragmentP;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Llamamos al fragment y le pasamos un DummyItem a través de la variable mCallback
            //que lo que hace es tener el listener escuchando para cuando se seleccione un item
            Product item = (Product) view.getTag();
            parentFragment.mCallback.onListProductSelected(item);
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Guardar la info del producto en los elementos del Layout
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNombreProducto.setText(mValues.get(position).getTitle());
        holder.mPrecioProducto.setText(mValues.get(position).getPrice() + " euros");
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //Intercambiar en el Fragment la lista vacía inicial del adapter por la lista de Productos recuperados de la base de datos
    public void swap(List<Product> dataset){
        this.mValues = dataset;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreProducto;
        public final TextView mPrecioProducto;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNombreProducto = view.findViewById(R.id.nombre_producto);
            mPrecioProducto = view.findViewById(R.id.precio_prod_lista);
        }

        @Override
        public String toString() { return ""; }
    }

}