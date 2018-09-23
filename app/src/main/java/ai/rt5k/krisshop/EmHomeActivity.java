package ai.rt5k.krisshop;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.widget.TextView;

public class EmHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    TextView txtName, txtMembershipNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_home);

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

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtName = navigationView.getHeaderView(0).findViewById(R.id.txtName);
        txtMembershipNo = navigationView.getHeaderView(0).findViewById(R.id.txtMembershipNo);

        // TODO: Get actual values from backend
        txtName.setText("Isaac Ashwin");
        txtMembershipNo.setText("Employee No: 0000000803");

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectDrawerItem(item);
        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.home:
                fragmentClass = EmHomeFragment.class;
                break;

            default:
                fragmentClass = EmHomeFragment.class;
        }



        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            MenuItem item = navigationView.getMenu().getItem(i);
            item.setChecked(false);
            if(item.getSubMenu() != null) {
                for(int j = 0; j < item.getSubMenu().size(); j++) {
                    item.getSubMenu().getItem(j).setChecked(false);
                }
            }
        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        //setTitle(menuItem.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
    }
}
