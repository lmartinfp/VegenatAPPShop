package asee.giis.unex.vegenatnavigationdrawer.ui.pedidos;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asee.giis.unex.vegenatnavigationdrawer.R;
import asee.giis.unex.vegenatnavigationdrawer.repository.model.local.Order;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Order}.
 */
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private List<Order> mValues;

    public MyOrderRecyclerViewAdapter(List<Order> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Datos para el fragment que contiene un item de la lista
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.n_order.setText("Ref. pedido: " + mValues.get(position).getId_order());
        holder.date_order.setText(mValues.get(position).getDate());
        holder.total_price.setText(mValues.get(position).getTotal_price() + " euros");
        holder.delivery_address.setText("Entrega: " + mValues.get(position).getDelivery_address());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //Intercambiar en el Fragment la lista vacía inicial del adapter por la lista de Productos recuperados de la base de datos
    public void swap(List<Order> dataset){
        this.mValues = dataset;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView n_order;
        public final TextView date_order;
        public final TextView total_price;
        public final TextView delivery_address;
        public Order mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            //Localizamos los elementos para mostrar info del pedido
            n_order =  view.findViewById(R.id.n_pedido);
            date_order = view.findViewById(R.id.fecha_pedido);
            total_price = view.findViewById(R.id.precio_total_pedido);
            delivery_address = view.findViewById(R.id.direccion_entrega);
        }

        @Override
        public String toString() {
            return " ";
        }
    }
}