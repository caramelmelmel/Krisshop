package ai.rt5k.krisshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.OrderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerOrdersFragment extends Fragment {
    RecyclerView lstActiveOrders;
    OrderAdapter activeOrderAdapter;
    ArrayList<Order> orders;

    // TODO: Remove dummy data
    String[] names = {"PRODUCT 1", "PRODUCT 2", "PRODUCT 3"};
    String[] statuses = {"Onboard", "Packed", "Ordered"};
    float[] prices = {122f, 29f, 33.2f};
    String[] flightNos = {"SQ101", "SQ444", "SQ691"};

    public CustomerOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_customer_orders, container, false);

        orders = new ArrayList<>();

        // TODO: get actual data from backend
        for(int i = 0; i < names.length; i++) {
            Order o = new Order();
            o.name = names[i];
            o.price = prices[i];
            o.status = statuses[i];
            o.flightNumber = flightNos[i];
            orders.add(o);
        }

        lstActiveOrders = itemView.findViewById(R.id.lstActiveOrders);
        activeOrderAdapter = new OrderAdapter(orders);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lstActiveOrders.setLayoutManager(mLayoutManager);
        lstActiveOrders.setItemAnimator(new DefaultItemAnimator());
        lstActiveOrders.setAdapter(activeOrderAdapter);

        return itemView;
    }

}
