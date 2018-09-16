package ai.rt5k.krisshop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ProductAdapter;

public class CustomerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtName, txtMembershipNo;
    RecyclerView lstBestSellers, lstNewProducts;

    // TODO: Remove dummy data
    String[] names = {"PRECIOUS MOMENTS \"SINGAPORE GIRL\" (70TH ANNIVERSARY)", "Product 2", "Product 3", "Product 4"};
    float[] prices = {10.0f, 1.0f, 2.55f, 10.70f};

    ArrayList<Product> bestSellers;
    BestSellerAdapter bestSellerAdapter;
    ProductAdapter newProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        String s = "KRISSHOP";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.9f), 1,4, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(0.9f), 5,8, 0); // set size

        toolbar.setTitle(ss1);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set element values
        txtName = navigationView.getHeaderView(0).findViewById(R.id.txtName);
        txtMembershipNo = navigationView.getHeaderView(0).findViewById(R.id.txtMembershipNo);
        lstBestSellers = findViewById(R.id.lstBestSellers);
        lstNewProducts = findViewById(R.id.lstNewProducts);

        bestSellers = new ArrayList<>();

        // TODO: Get actual values from backend
        txtName.setText("Isaac Ashwin");
        txtMembershipNo.setText("Membership No: KF 8831139803");

        // TODO: Get actual products from backend
        for(int i = 0; i < names.length; i++) {
            Product p = new Product();
            p.name = names[i];
            p.price = prices[i];

            bestSellers.add(p);
        }

        bestSellerAdapter = new BestSellerAdapter(bestSellers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomerHomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        lstBestSellers.setLayoutManager(mLayoutManager);
        lstBestSellers.setItemAnimator(new DefaultItemAnimator());
        lstBestSellers.setAdapter(bestSellerAdapter);

        newProductAdapter = new ProductAdapter(bestSellers);
        RecyclerView.LayoutManager productManager = new GridLayoutManager(CustomerHomeActivity.this, 2);
        lstNewProducts.setLayoutManager(productManager);
        lstNewProducts.setItemAnimator(new DefaultItemAnimator());
        lstNewProducts.setAdapter(newProductAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
