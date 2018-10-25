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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import ai.rt5k.krisshop.RecyclerViewAdapters.OrderAdapter;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerOrdersFragment extends Fragment {
    private static final int ORDER_DETAILS_INTENT = 0;

    MainApplication m;

    RecyclerView lstActiveOrders, lstCompletedOrders;
    TextView txtActiveOrders, txtCompletedOrders;
    OrderAdapter activeOrderAdapter, completedOrderAdapter;
    ArrayList<Order> orders, completedOrders;

    public CustomerOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View itemView = inflater.inflate(R.layout.fragment_customer_orders, container, false);

        m = (MainApplication) getActivity().getApplicationContext();

        txtActiveOrders = itemView.findViewById(R.id.txtActiveOrders);
        txtCompletedOrders = itemView.findViewById(R.id.txtCompletedOrders);
        lstActiveOrders = itemView.findViewById(R.id.lstActiveOrders);
        lstCompletedOrders = itemView.findViewById(R.id.lstCompletedOrders);

        loadOrders();

        return itemView;
    }

    public void loadOrders() {
        orders = new ArrayList<>();
        completedOrders = new ArrayList<>();

        StringRequest orderRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/view_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for(int i = 0; i < responseArray.length(); i++) {
                        Order order = new Order();
                        JSONObject orderObject = responseArray.getJSONObject(i);

                        if(orderObject.getString("status").equals("Cancelled")) continue;

                        order.id = orderObject.getString("id");
                        order.status = orderObject.getString("status");
                        order.flightNumber = orderObject.getString("flight_no");
                        order.price = orderObject.getDouble("price");
                        order.products = new ArrayList<>();
                        order.quantities = new HashMap<>();
                        order.totalQuantity = 0;

                        for(int j = 0; j < orderObject.getJSONArray("product_id").length(); j++) {
                            Product product = new Product();

                            product.id = orderObject.getJSONArray("product_id").getInt(j);
                            product.name = orderObject.getJSONArray("product_names").getString(j);
                            product.description = orderObject.getJSONArray("product_descr").getString(j);
                            product.price = Double.parseDouble(orderObject.getJSONArray("product_prices").getString(j).substring(1).replace(",", ""));
                            product.imageUrl = orderObject.getJSONArray("product_images").getString(j);
                            product.miles = Integer.parseInt(orderObject.getJSONArray("product_miles").getString(j).substring(0, orderObject.getJSONArray("product_miles").getString(j).indexOf(' ')).replace(",", ""));

                            order.products.add(product);
                            order.quantities.put(product, orderObject.getJSONArray("product_quantities").getInt(j));
                            order.totalQuantity += orderObject.getJSONArray("product_quantities").getInt(j);
                        }

                        if(order.status.equals("Completed")) {
                            completedOrders.add(order);
                        }
                        else {
                            orders.add(order);
                        }
                    }

                    if(orders.size() == 0) {
                        txtActiveOrders.setVisibility(View.GONE);
                    }
                    if(completedOrders.size() == 0) {
                        txtCompletedOrders.setVisibility(View.GONE);
                    }
                    activeOrderAdapter = new OrderAdapter(orders, getActivity());
                    activeOrderAdapter.setOnClickListener(new ClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("CustomerOrdersFragment", "Active order: " + position);
                            Intent orderDetailsIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("order", orders.get(position));
                            orderDetailsIntent.putExtra("orderBundle", b);
                            startActivityForResult(orderDetailsIntent, ORDER_DETAILS_INTENT);
                        }
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    lstActiveOrders.setLayoutManager(mLayoutManager);
                    lstActiveOrders.setItemAnimator(new DefaultItemAnimator());
                    lstActiveOrders.setAdapter(activeOrderAdapter);
                    lstActiveOrders.setNestedScrollingEnabled(false);

                    completedOrderAdapter = new OrderAdapter(completedOrders, getActivity());
                    completedOrderAdapter.setOnClickListener(new ClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("CustomerOrdersFragment", "Completed order: " + position);
                            Intent orderDetailsIntent = new Intent(getActivity(), OrderDetailsActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("order", completedOrders.get(position));
                            orderDetailsIntent.putExtra("orderBundle", b);
                            startActivityForResult(orderDetailsIntent, ORDER_DETAILS_INTENT);
                        }
                    });
                    RecyclerView.LayoutManager completedLayoutManager = new LinearLayoutManager(getActivity());
                    lstCompletedOrders.setLayoutManager(completedLayoutManager);
                    lstCompletedOrders.setItemAnimator(new DefaultItemAnimator());
                    lstCompletedOrders.setAdapter(completedOrderAdapter);
                    lstCompletedOrders.setNestedScrollingEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("uid", m.sessionId);
                return params;
            }
        };

        m.mainQueue.add(orderRequest);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ORDER_DETAILS_INTENT && resultCode == RESULT_OK) {
            loadOrders();
        }
    }
}
