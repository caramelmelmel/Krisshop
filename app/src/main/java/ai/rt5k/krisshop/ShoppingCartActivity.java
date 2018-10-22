package ai.rt5k.krisshop;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.RecyclerViewAdapters.CartAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import info.hoang8f.android.segmented.SegmentedGroup;

public class ShoppingCartActivity extends AppCompatActivity {
    MainApplication m;

    RecyclerView lstCartItems;
    CartAdapter cartAdapter;
    Button btnCheckout;
    TextView txtCartEmpty;

    static final int[] imageButtons = new int[] {
            R.id.imgColor1,
            R.id.imgColor2,
            R.id.imgColor3,
            R.id.imgColor4,
            R.id.imgColor5,
            R.id.imgColor6,
            R.id.imgColor7,
            R.id.imgColor8
    };

    static final String[] colors = new String[] {
            "#FF4333",
            "#FFFF6A",
            "#FF9800",
            "#AAFF48",
            "#2979FF",
            "#10FFC9",
            "#FF52FF",
            "#FF75A5"
    };

    boolean[] selectedImage = new boolean[] {
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };

    GradientDrawable[] selected = new GradientDrawable[8];
    GradientDrawable[] unselected = new GradientDrawable[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);

        String s = "KRISSHOP";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.9f), 1,4, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(0.9f), 5,8, 0); // set size

        toolbar.setTitle(ss1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m = (MainApplication) getApplicationContext();

        lstCartItems = findViewById(R.id.lstCartItems);
        btnCheckout = findViewById(R.id.btnCheckout);
        txtCartEmpty = findViewById(R.id.txtEmptyCart);

        checkEmpty();

        cartAdapter = new CartAdapter(m.cart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lstCartItems.setLayoutManager(mLayoutManager);
        lstCartItems.setItemAnimator(new DefaultItemAnimator());
        lstCartItems.setAdapter(cartAdapter);

        cartAdapter.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent viewProductIntent = new Intent(ShoppingCartActivity.this, CustomerViewProductActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("product", cartAdapter.products.get(position));
                viewProductIntent.putExtra("productBundle", b);
                startActivity(viewProductIntent);
            }
        });
        cartAdapter.setButtonClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Product p = cartAdapter.products.get(position);
                m.cart.remove(p);
                cartAdapter.products = new ArrayList<>(m.cart.keySet());
                cartAdapter.notifyDataSetChanged();
                Snackbar.make(lstCartItems, "Removed " + p.description, Snackbar.LENGTH_SHORT).show();
                checkEmpty();
            }
        });

        for(int i = 0; i < imageButtons.length; i++) {
            unselected[i] = new GradientDrawable();
            unselected[i].setShape(GradientDrawable.OVAL);
            unselected[i].setColor(Color.parseColor(colors[i]));
            unselected[i].setSize(144, 144);

            selected[i] = new GradientDrawable();
            selected[i].setShape(GradientDrawable.OVAL);
            selected[i].setColor(Color.parseColor(colors[i]));
            selected[i].setSize(159, 159);
            selected[i].setStroke(8, Color.parseColor("#999999"));//getResources().getColor(R.color.colorPrimary));
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ShoppingCartActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_checkout);

                SegmentedGroup btnDelivery = dialog.findViewById(R.id.btnDelivery);
                btnDelivery.setTintColor(getResources().getColor(R.color.colorPrimary));

                for(int i = 0; i < imageButtons.length; i++) {
                    final ImageButton img = dialog.findViewById(imageButtons[i]);
                    img.setBackground(unselected[i]);
                    img.setTag(i);
                    img.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            img.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_in));
                            for(int i = 0; i < imageButtons.length; i++) {
                                selectedImage[i] = false;
                                final ImageButton img = dialog.findViewById(imageButtons[i]);
                                img.setBackground(unselected[i]);
                            }
                            selectedImage[(Integer) v.getTag()] = true;
                            img.setBackground(selected[(Integer) v.getTag()]);
                        }
                    });
                }
                dialog.show();

                dialog.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Iterator<Map.Entry<Product, Integer>> it = m.cart.entrySet().iterator();

                        final JSONArray products = new JSONArray();
                        while(it.hasNext()) {
                            Map.Entry<Product, Integer> entry = it.next();
                            Product p = entry.getKey();
                            int quantity = entry.getValue();

                            JSONObject product = new JSONObject();
                            try {
                                product.put("id", p.id);
                                product.put("quantity", quantity);
                                products.put(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        StringRequest checkoutRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/add_order", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject responseObject = new JSONObject(response);
                                    if(responseObject.getBoolean("success")) {
                                        Snackbar.make(lstCartItems, "Order #" + responseObject.getString("order_id").toUpperCase() + " checked out!", Snackbar.LENGTH_SHORT).show();
                                        m.cart.clear();
                                        dialog.dismiss();
                                        btnCheckout.setEnabled(false);
                                        (new Timer()).schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent resultIntent = new Intent();
                                                        try {
                                                            resultIntent.putExtra("order_id", responseObject.getString("order_id"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        setResult(RESULT_OK, resultIntent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }, 1000);
                                    }
                                    else {
                                        Snackbar.make(lstCartItems, "Checkout failed: " + responseObject.getString("message"), Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LoginActivity", error.toString());
                            }
                        }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<>();
                                params.put("uid", m.sessionId);
                                params.put("flight_no", "SQ774");
                                params.put("products", products.toString());
                                String selectedColor = "";
                                for(int i = 0; i < colors.length; i++) {
                                    if(selectedImage[i]) {
                                        selectedColor = colors[i];
                                        break;
                                    }
                                }
                                params.put("color", selectedColor);
                                return params;
                            }
                        };

                        m.mainQueue.add(checkoutRequest);
                    }
                });
            }
        });
    }

    public void checkEmpty() {
        if(m.cart.size() != 0) {
            txtCartEmpty.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.VISIBLE);
        }
        else {
            btnCheckout.setVisibility(View.GONE);
            txtCartEmpty.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
