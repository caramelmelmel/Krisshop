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


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerOrdersFragment extends Fragment {
    MainApplication m;

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
        final View itemView = inflater.inflate(R.layout.fragment_customer_orders, container, false);

        m = (MainApplication) getActivity().getApplicationContext();

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

        return itemView;
    }

}
