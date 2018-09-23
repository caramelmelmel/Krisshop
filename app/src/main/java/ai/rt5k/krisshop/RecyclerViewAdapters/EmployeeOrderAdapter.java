package ai.rt5k.krisshop.RecyclerViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.R;

public class EmployeeOrderAdapter extends RecyclerView.Adapter<EmployeeOrderAdapter.ViewHolder> {
    ArrayList<Order> orders;
    private static DecimalFormat df2 = new DecimalFormat(".00");
    private ClickListener clickListener;
    private Context context;

    public EmployeeOrderAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    public void setOnClickListener(ClickListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_order_employee, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Order order = orders.get(i);
        viewHolder.txtOrderName.setText(order.name);
        viewHolder.txtOrderID.setText(order.id);
        viewHolder.txtFlightNumber.setText(order.flightNumber);
        viewHolder.position = i;

        if(clickListener != null) {
            viewHolder.crdOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                clickListener.onItemClick(viewHolder.position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int position;

        public View itemView;
        public CardView crdOrder;
        public TextView txtOrderID, txtOrderName, txtFlightNumber;
        public ImageView imgOrder;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            crdOrder = view.findViewById(R.id.crdOrder);

            imgOrder = view.findViewById(R.id.imgOrder);
            txtOrderID = view.findViewById(R.id.txtOrderID);
            txtOrderName = view.findViewById(R.id.txtOrderName);
            txtFlightNumber = view.findViewById(R.id.txtFlightNumber);
        }
    }
}
