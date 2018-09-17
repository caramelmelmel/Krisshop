package ai.rt5k.krisshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.RecyclerViewAdapters.CartAdapter;

public class ShoppingCartActivity extends AppCompatActivity {

    RecyclerView lstCartItems;
    CartAdapter cartAdapter;
    ArrayList<Product> cartItems;

    // TODO: Remove dummy data
    String[] names = {"PRODUCT 1", "PRODUCT 2", "PRODUCT 3"};
    float[] prices = {122f, 29f, 33.2f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstCartItems = findViewById(R.id.lstCartItems);
        cartItems = new ArrayList<>();

        // TODO: Get actual user cart contents
        for (int i = 0; i < names.length; i++) {
            Product p = new Product(names[i], prices[i]);
            cartItems.add(p);
        }
        cartAdapter = new CartAdapter(cartItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lstCartItems.setLayoutManager(mLayoutManager);
        lstCartItems.setItemAnimator(new DefaultItemAnimator());
        lstCartItems.setAdapter(cartAdapter);
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
