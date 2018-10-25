package ai.rt5k.krisshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.ModelObjects.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmHomeFragment extends Fragment {
    EditText edtFlightNumber;
    Button btnStartPacking;

    public EmHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MainApplication m = (MainApplication) getContext().getApplicationContext();

        final View rootView = inflater.inflate(R.layout.fragment_em_home, container, false);

        edtFlightNumber = rootView.findViewById(R.id.edtFlightNumber);
        btnStartPacking = rootView.findViewById(R.id.btnStartPacking);

        btnStartPacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String flightNo = edtFlightNumber.getText().toString();

                // TODO: Uncomment empty string check
                if(flightNo.equals("")) {
                    Snackbar.make(rootView, "Please enter a flight number", Snackbar.LENGTH_SHORT).setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                }

                StringRequest flightRequest = new StringRequest(MainApplication.SERVER_URL + "/orders_by_flight/" + flightNo, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray responseArray = new JSONArray(response);
                            ArrayList<Order> orders = new ArrayList<>();

                            for(int i = 0; i < responseArray.length(); i++) {
                                JSONObject orderObject = responseArray.getJSONObject(i);
                                Order order = new Order();

                                order.id = orderObject.getString("id");
                                order.status = orderObject.getString("status");
                                order.flightNumber = orderObject.getString("flight_no");
                                order.color = orderObject.getString("color");
                                order.totalQuantity = 0;

                                order.products = new ArrayList<>();

                                for(int j = 0; j < orderObject.getJSONArray("product_quantities").length(); j++) {
                                    Product p = new Product();

                                    p.id = orderObject.getJSONArray("product_id").getInt(j);
                                    p.name = orderObject.getJSONArray("product_names").getString(j);
                                    p.imageUrl = orderObject.getJSONArray("product_images").getString(j);
                                    p.description = orderObject.getJSONArray("product_descr").getString(j);
                                    p.price = Double.parseDouble(orderObject.getJSONArray("product_prices").getString(j).substring(1).replace(",", ""));
                                    p.miles = Integer.parseInt(orderObject.getJSONArray("product_miles").getString(j).substring(0, orderObject.getJSONArray("product_miles").getString(j).indexOf(' ')).replace(",", ""));
                                    p.quantity = orderObject.getJSONArray("product_quantities").getInt(j);

                                    order.products.add(p);
                                    order.totalQuantity += orderObject.getJSONArray("product_quantities").getInt(j);
                                }

                                orders.add(order);
                            }
                            Intent orderIntent = new Intent(getContext(), EmOrdersActivity.class);
                            orderIntent.putExtra("orders", orders);
                            startActivity(orderIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginActivity", error.toString());
                    }
                });
                m.mainQueue.add(flightRequest);
            }
        });
        return rootView;
    }

}
