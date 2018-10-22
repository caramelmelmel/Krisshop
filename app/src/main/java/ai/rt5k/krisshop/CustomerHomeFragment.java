package ai.rt5k.krisshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import ai.rt5k.krisshop.RecyclerViewAdapters.ProductAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerHomeFragment extends Fragment {
    RecyclerView lstBestSellers, lstNewProducts;

    // TODO: Remove dummy data
    String[] names = {"PRECIOUS MOMENTS \"SINGAPORE GIRL\" (70TH ANNIVERSARY)", "Product 2", "Product 3", "Product 4"};
    float[] prices = {10.0f, 1.0f, 2.55f, 10.70f};

    ArrayList<Product> bestSellers;
    BestSellerAdapter bestSellerAdapter;
    ProductAdapter newProductAdapter;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_customer_home, container, false);

        final MainApplication m = (MainApplication) getActivity().getApplicationContext();

        lstBestSellers = itemView.findViewById(R.id.lstBestSellers);
        lstNewProducts = itemView.findViewById(R.id.lstNewProducts);

        bestSellers = new ArrayList<>();

        StringRequest bestsellerRequest = new StringRequest(Request.Method.GET, MainApplication.SERVER_URL + "/bestsellers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for(int i = 0; i < responseArray.length(); i++) {
                        JSONObject o = responseArray.getJSONObject(i);
                        Product p = new Product();
                        p.name = o.getString("name");
                        p.price = Float.parseFloat(o.getString("price").substring(1).replace(",",""));
                        p.miles = Integer.parseInt(o.getString("miles").split(" ")[0].replace(",", ""));
                        p.imageUrl = o.getString("image");
                        p.description = o.getString("content");
                        bestSellers.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                bestSellerAdapter = new BestSellerAdapter(bestSellers);
                bestSellerAdapter.setOnClickListener(new ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Log.d("CustomerHomeActivity", position + "");
                        Intent viewProductIntent = new Intent(getContext(), CustomerViewProductActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("product", bestSellers.get(position));
                        viewProductIntent.putExtra("productBundle", b);
                        startActivity(viewProductIntent);
                    }
                });

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                lstBestSellers.setLayoutManager(mLayoutManager);
                lstBestSellers.setItemAnimator(new DefaultItemAnimator());
                lstBestSellers.setAdapter(bestSellerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        m.mainQueue.add(bestsellerRequest);

        bestSellerAdapter = new BestSellerAdapter(bestSellers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        lstBestSellers.setLayoutManager(mLayoutManager);
        lstBestSellers.setItemAnimator(new DefaultItemAnimator());
        lstBestSellers.setAdapter(bestSellerAdapter);

        StringRequest productRequest = new StringRequest(Request.Method.GET, MainApplication.SERVER_URL + "/products", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final ArrayList<Product> products = new ArrayList<>();

                try {
                    JSONArray responseArray = new JSONArray(response);
                    for(int i = 0; i < responseArray.length(); i++) {
                        JSONObject o = responseArray.getJSONObject(i);
                        Product p = new Product();
                        p.name = o.getString("name");
                        p.price = Float.parseFloat(o.getString("price").substring(1).replace(",",""));
                        p.miles = Integer.parseInt(o.getString("miles").split(" ")[0].replace(",", ""));
                        p.imageUrl = o.getString("image");
                        p.description = o.getString("content");
                        products.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                newProductAdapter = new ProductAdapter(products);
                newProductAdapter.setOnClickListener(new ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // TODO: Load product view activity
                        Intent viewProductIntent = new Intent(getContext(), CustomerViewProductActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("product", products.get(position));
                        viewProductIntent.putExtra("productBundle", b);
                        startActivity(viewProductIntent);
                    }
                });
                newProductAdapter.setButtonClickListener(new ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Product product = products.get(position);
                        if(m.cart.containsKey(product)) {
                            m.cart.put(product, m.cart.get(product) + 1);
                        }
                        else {
                            m.cart.put(product, 1);
                        }
                        Snackbar.make(lstNewProducts, "Added to cart", Snackbar.LENGTH_SHORT).show();
                    }
                });

                RecyclerView.LayoutManager productManager = new GridLayoutManager(getActivity(), 2);
                lstNewProducts.setLayoutManager(productManager);
                lstNewProducts.setItemAnimator(new DefaultItemAnimator());
                lstNewProducts.setAdapter(newProductAdapter);
                lstNewProducts.setNestedScrollingEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        m.mainQueue.add(productRequest);

        return itemView;
    }
}
