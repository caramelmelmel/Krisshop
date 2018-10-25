package ai.rt5k.krisshop;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ai.rt5k.krisshop.ModelObjects.Order;

public class EmOrderDetailsActivity extends AppCompatActivity {
    private static final int EMPLOYEE_QRCODE = 0;

    Order order;
    Button btnConfirmRecieved;
    TextView txtOrderId, txtFlightNumber;
    TextView txtOrderCollected;
    ListView lstProducts;
    RelativeLayout layoutProgressSection;
    ProgressBar prgStatus;
    ImageView checkpointPacked, checkpointOnboard, checkpointArrived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = (Order) getIntent().getBundleExtra("orderBundle").getSerializable("order");

        getSupportActionBar().setTitle("Order #" + order.id.toUpperCase());

        txtOrderId = findViewById(R.id.txt_order_id);
        txtFlightNumber = findViewById(R.id.txt_flight_number);
        lstProducts = findViewById(R.id.lstProducts);
        txtOrderCollected = findViewById(R.id.txtOrderCollected);
        layoutProgressSection = findViewById(R.id.layoutProgressSection);
        btnConfirmRecieved = findViewById(R.id.btnConfirmReceived);
        prgStatus = findViewById(R.id.prgStatus);
        checkpointPacked = findViewById(R.id.checkpointPacked);
        checkpointOnboard = findViewById(R.id.checkpointOnboard);
        checkpointArrived = findViewById(R.id.checkpointArrived);

        btnConfirmRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderDetailsIntent = new Intent(EmOrderDetailsActivity.this, EmCameraActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("order", order);
                orderDetailsIntent.putExtra("orderBundle", b);
                startActivityForResult(orderDetailsIntent, EMPLOYEE_QRCODE);
            }
        });

        txtOrderId.setText("#" + order.id.toUpperCase());
        txtFlightNumber.setText(order.flightNumber);
        lstProducts.setAdapter(new ProductAdapter());
        setStatus();
    }

    public void setStatus() {
        switch (order.status) {
            case "Arrived":
                prgStatus.setProgress(Math.max(getResources().getInteger(R.integer.progress_arrived), prgStatus.getProgress()));
                checkpointArrived.setImageDrawable(getResources().getDrawable(R.drawable.checkpoint_active));
            case "Onboard":
                prgStatus.setProgress(Math.max(getResources().getInteger(R.integer.progress_onboard), prgStatus.getProgress()));
                checkpointOnboard.setImageDrawable(getResources().getDrawable(R.drawable.checkpoint_active));
            case "Packed":
                prgStatus.setProgress(Math.max(getResources().getInteger(R.integer.progress_packed), prgStatus.getProgress()));
                checkpointPacked.setImageDrawable(getResources().getDrawable(R.drawable.checkpoint_active));
        }

        if(!order.status.equals("Ordered")) {
            btnConfirmRecieved.setEnabled(false);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EMPLOYEE_QRCODE && resultCode == RESULT_OK) {
            order.status = "Packed";
            setStatus();
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

    class ProductAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return order.products.size();
        }

        @Override
        public Object getItem(int i) {
            return order.products.get(i);
        }

        @Override
        public long getItemId(int i) {
            return order.products.get(i).id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView itemView;

            if(view != null) {
                itemView = (TextView) view;
            }
            else {
                itemView = new TextView(viewGroup.getContext());
            }

            itemView.setText(order.products.get(i).name + " x" + order.products.get(i).quantity);
            itemView.setTypeface(Typeface.createFromAsset(getAssets(), "proxima_nova_regular.ttf"));
            return itemView;
        }
    }
}
