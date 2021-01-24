package asee.giis.unex.vegenatnavigationdrawer.ui.lista_compra;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.ProductWithQuantity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ProductWithQuantity}.
 */
public class MyProductShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<MyProductShoppingListRecyclerViewAdapter.ViewHolder> {

    private List<ProductWithQuantity> mValues;
    //Para poder llamar al fragment con la interfaz tengo que pasarle el fragment
    private ProductShoppingListFragment parentFragment;

    public MyProductShoppingListRecyclerViewAdapter(List<ProductWithQuantity> items, ProductShoppingListFragment productShoppingListFragment) {
        mValues = items;
        parentFragment = productShoppingListFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product_shopping, parent, false);

        Button button = view.findViewById(R.id.eliminar_prod);
        button.setOnClickListener(v -> {
            ProductWithQuantity productWithQuantity = (ProductWithQuantity) view.getTag();
            parentFragment.mCallback.onProductShoppingDelete(productWithQuantity);
        });

        return new ViewHolder(view);
    }

    /**
     * Datos para el fragment que contiene un item de la lista
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre_producto.setText(mValues.get(position).getProduct_name());
        holder.cantidad_producto.setText("x" + mValues.get(position).getQuantity() + " uds");
        holder.precio_producto.setText(String.valueOf(mValues.get(position).getPrice()) + " euros/ud");
        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Intercambiar la lista de la recyclerview cuando se produzcan cambios
     */
    public void swap(List<ProductWithQuantity> dataset){
        this.mValues = dataset;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nombre_producto;
        public final TextView cantidad_producto;
        public final TextView precio_producto;
        public ProductWithQuantity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            //Obtenemos las referencas de los elementos de la vista
            nombre_producto = view.findViewById(R.id.nombre_producto);
            cantidad_producto = view.findViewById(R.id.cantidad_producto_lista);
            precio_producto = view.findViewById(R.id.precio_prod_lista);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}