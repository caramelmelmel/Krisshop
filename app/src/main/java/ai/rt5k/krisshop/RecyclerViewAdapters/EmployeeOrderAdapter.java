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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.R;

public class EmployeeOrderAdapter extends RecyclerView.Adapter<EmployeeOrderAdapter.ViewHolder> {
    ArrayList<Order> orders;
    private static DecimalFormat df2 = new DecimalFormat(".00");
    private static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
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
        viewHolder.txtOrderName.setText(viewHolder.txtOrderName.getContext().getResources().getQuantityString(R.plurals.order_items, order.totalQuantity, order.totalQuantity));
        viewHolder.txtOrderID.setText("ORDER #" + order.id.toUpperCase());
        viewHolder.txtFlightNumber.setText(order.flightNumber);
        try {
            viewHolder.txtOrderDate.setText("PLACED: " + outputFormat.format(inputFormat.parse(order.orderedOn)).toUpperCase());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        public TextView txtOrderID, txtOrderName, txtFlightNumber, txtOrderDate;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            crdOrder = view.findViewById(R.id.crdOrder);

            txtOrderID = view.findViewById(R.id.txtOrderID);
            txtOrderName = view.findViewById(R.id.txtOrderName);
            txtFlightNumber = view.findViewById(R.id.txtFlightNumber);
            txtOrderDate = view.findViewById(R.id.txtOrderDate);
        }
    }
}
