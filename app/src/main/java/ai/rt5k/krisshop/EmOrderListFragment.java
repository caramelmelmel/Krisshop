package ai.rt5k.krisshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import ai.rt5k.krisshop.RecyclerViewAdapters.EmployeeOrderAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.OrderAdapter;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmOrderListFragment extends Fragment {
    private static final String ARG_FILTER = "filter";
    private static final int ORDER_DETAIL_INTENT = 0;

    private String filter;

    public EmOrderListFragment() {
        // Required empty public constructor
    }

    public static EmOrderListFragment newInstance(String filter) {
        EmOrderListFragment fragment = new EmOrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filter = getArguments().getString(ARG_FILTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_em_order_list, container, false);

        final ArrayList<Order> orders = new ArrayList<>();

        for(Order o : ((EmOrdersActivity) getActivity()).orders) {
            if(o.status.equals(filter)) {
                orders.add(o);
            }
        }

        RecyclerView lstOrders = rootView.findViewById(R.id.lstOrders);
        EmployeeOrderAdapter orderAdapter = new EmployeeOrderAdapter(orders, getActivity());
        orderAdapter.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("EmOrderListFragment", "Active order: " + position);
                Intent orderDetailsIntent = new Intent(getActivity(), EmOrderDetailsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", orders.get(position));
                orderDetailsIntent.putExtra("orderBundle", b);
                startActivityForResult(orderDetailsIntent, ORDER_DETAIL_INTENT);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lstOrders.setLayoutManager(mLayoutManager);
        lstOrders.setItemAnimator(new DefaultItemAnimator());
        lstOrders.setAdapter(orderAdapter);
        lstOrders.setNestedScrollingEnabled(false);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ORDER_DETAIL_INTENT && resultCode == RESULT_OK) {
            // Perform update of data
        }
    }
}
