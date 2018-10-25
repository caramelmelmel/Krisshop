package ai.rt5k.krisshop;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Order;

public class EmOrdersActivity extends AppCompatActivity {
    public ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orders = (ArrayList<Order>) getIntent().getSerializableExtra("orders");

        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_em_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Orders");
        ViewPager pgrOrders = findViewById(R.id.pgrOrders);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        pgrOrders.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pgrOrders);
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

    class PagerAdapter extends FragmentStatePagerAdapter {
        private final String[] titles = {"Pending", "Packed", "Onboard"};
        private final String[] filters = {"Ordered", "Packed", "Onboard"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return EmOrderListFragment.newInstance(filters[position]);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
