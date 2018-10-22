package ai.rt5k.krisshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import ai.rt5k.krisshop.ModelObjects.Order;
import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.Util.ImageThread;

public class CustomerViewProductActivity extends AppCompatActivity {
    private static DecimalFormat df2 = new DecimalFormat(".00");

    ImageView imgItem;
    TextView txtItemName, txtItemDescription, txtItemPrice, txtItemMiles;
    Button btnAddToCart;

    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_product);
        Toolbar toolbar = findViewById(R.id.toolbar);

        String s = "KRISSHOP";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.9f), 1,4, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(0.9f), 5,8, 0); // set size

        toolbar.setTitle(ss1);
        setSupportActionBar(toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        product = (Product) getIntent().getBundleExtra("productBundle").getSerializable("product");

        imgItem = findViewById(R.id.imgItem);
        txtItemName = findViewById(R.id.txtItemName);
        txtItemDescription = findViewById(R.id.txtItemDescription);
        txtItemPrice = findViewById(R.id.txtItemPrice);
        txtItemMiles = findViewById(R.id.txtItemMiles);

        txtItemName.setText(product.name);
        txtItemDescription.setText(product.description);
        txtItemPrice.setText("$" + df2.format(product.price));
        txtItemMiles.setText(NumberFormat.getNumberInstance(Locale.US).format(product.miles) + " miles ");

        if(product.image != null) {
            imgItem.setImageBitmap(product.image);
        }
        else if(product.imageUrl != null) {
            Log.d("ProductAdapter", "Starting thread");
            ImageThread t = new ImageThread(product.imageUrl, imgItem);
            t.start();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
