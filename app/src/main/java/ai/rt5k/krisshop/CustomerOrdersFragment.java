package ai.rt5k.krisshop;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import ai.rt5k.krisshop.RecyclerViewAdapters.OrderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerOrdersFragment extends Fragment {
    RecyclerView lstActiveOrders, lstCompletedOrders;
    OrderAdapter activeOrderAdapter, completedOrderAdapter;
    ArrayList<Order> orders, completedOrders;

    // TODO: Remove dummy data
    String[] ids = {"#10000", "#00102", "#67219"};
    String[] names = {"PRODUCT 1", "PRODUCT 2", "PRODUCT 3"};
    String[] statuses = {"Onboard", "Packed", "Ordered"};
    float[] prices = {122f, 29f, 33.2f};
    String[] flightNos = {"SQ101", "SQ444", "SQ691"};

    String[] completednames = {"PRODUCT 1", "PRODUCT 2", "PRODUCT 3", "PRODUCT 4"};
    String[] completedstatuses = {"Completed", "Completed", "Completed", "Completed"};
    float[] completedprices = {122f, 29f, 33.2f, 85.0f};
    String[] completedflightNos = {"SQ101", "SQ444", "SQ691", "SQ71"};

    public CustomerOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_customer_orders, container, false);

        orders = new ArrayList<>();
        completedOrders = new ArrayList<>();

        // TODO: get actual data from backend
        for(int i = 0; i < names.length; i++) {
            Order o = new Order();
            o.id = ids[i];
            o.name = names[i];
            o.price = prices[i];
            o.status = statuses[i];
            o.flightNumber = flightNos[i];
            orders.add(o);
        }

        for(int i = 0; i < completednames.length; i++) {
            Order o = new Order();
            o.name = completednames[i];
            o.price = completedprices[i];
            o.status = completedstatuses[i];
            o.flightNumber = completedflightNos[i];
            completedOrders.add(o);
        }

        lstActiveOrders = itemView.findViewById(R.id.lstActiveOrders);
        activeOrderAdapter = new OrderAdapter(orders, getActivity());
        activeOrderAdapter.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CustomerOrdersFragment", "Active order: " + position);
                Intent orderDetailsIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", orders.get(position));
                orderDetailsIntent.putExtra("orderBundle", b);
                startActivity(orderDetailsIntent);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lstActiveOrders.setLayoutManager(mLayoutManager);
        lstActiveOrders.setItemAnimator(new DefaultItemAnimator());
        lstActiveOrders.setAdapter(activeOrderAdapter);
        lstActiveOrders.setNestedScrollingEnabled(false);

        lstCompletedOrders = itemView.findViewById(R.id.lstCompletedOrders);
        completedOrderAdapter = new OrderAdapter(completedOrders, getActivity());
        completedOrderAdapter.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CustomerOrdersFragment", "Completed order: " + position);
                Intent orderDetailsIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", completedOrders.get(position));
                orderDetailsIntent.putExtra("orderBundle", b);
                startActivity(orderDetailsIntent);
            }
        });
        RecyclerView.LayoutManager completedLayoutManager = new LinearLayoutManager(getActivity());
        lstCompletedOrders.setLayoutManager(completedLayoutManager);
        lstCompletedOrders.setItemAnimator(new DefaultItemAnimator());
        lstCompletedOrders.setAdapter(completedOrderAdapter);
        lstCompletedOrders.setNestedScrollingEnabled(false);

        return itemView;
    }

}
