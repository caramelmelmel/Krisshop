package ai.rt5k.krisshop.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Order> orders;
    private static DecimalFormat df2 = new DecimalFormat(".00");
    private ClickListener clickListener;
    private Context context;

    public OrderAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    public void setOnClickListener(ClickListener listener) {
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_order, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Order order = orders.get(i);
        viewHolder.txtItemName.setText(viewHolder.txtItemName.getContext().getResources().getQuantityString(R.plurals.order_items, order.totalQuantity, order.totalQuantity));
        viewHolder.txtPrice.setText("$" + df2.format(order.price));
        viewHolder.txtOrderStatus.setText(order.status.toUpperCase());
        viewHolder.txtFlightNumber.setText(order.flightNumber);
        viewHolder.position = i;

        int statusColor;
        switch (order.status) {
            case "Completed":
                statusColor = context.getResources().getColor(R.color.colorGrey);
                viewHolder.viwStatus.setActivated(false);
                viewHolder.viwStatus.setEnabled(true);
                break;
            case "Onboard":
                statusColor = context.getResources().getColor(R.color.colorOK);
                viewHolder.viwStatus.setActivated(true);
                viewHolder.viwStatus.setEnabled(true);
                break;
            case "Packed":
            case "Ordered":
                statusColor = context.getResources().getColor(R.color.colorAlert);
                viewHolder.viwStatus.setActivated(true);
                viewHolder.viwStatus.setEnabled(false);
                break;
            default:
                statusColor = context.getResources().getColor(R.color.colorAlert);
                break;
        }

        viewHolder.txtOrderStatus.setTextColor(statusColor);
        viewHolder.txtPrice.setTextColor(statusColor);

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
        public TextView txtOrderStatus, txtItemName, txtFlightNumber, txtPrice;
        public View viwStatus;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            crdOrder = view.findViewById(R.id.crdOrder);

            viwStatus = view.findViewById(R.id.viwStatus);
            txtOrderStatus = view.findViewById(R.id.txtOrderStatus);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtItemName = view.findViewById(R.id.txtItemName);
            txtFlightNumber = view.findViewById(R.id.txtFlightNumber);
        }
    }
}
