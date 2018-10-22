package ai.rt5k.krisshop;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import ai.rt5k.krisshop.ModelObjects.Order;

public class OrderDetailsActivity extends AppCompatActivity {
    Order order;
    Button btnConfirmRecieved;
    TextView txtOrderId, txtAmount, txtFlightNumber;
    TextView txtOrderCollected;
    RelativeLayout layoutProgressSection;

    private static final int customerQRCodeRequest = 1;
    private static final String TAG = "OrderDetailsActivity";
    private static DecimalFormat df2 = new DecimalFormat(".00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);

        String s = "KRISSHOP";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.9f), 1,4, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(0.9f), 5,8, 0); // set size

        toolbar.setTitle(ss1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = (Order) getIntent().getBundleExtra("orderBundle").getSerializable("order");

        txtOrderId = findViewById(R.id.txt_order_id);
        txtAmount = findViewById(R.id.txt_amount);
        txtFlightNumber = findViewById(R.id.txt_flight_number);

        txtOrderCollected = findViewById(R.id.txtOrderCollected);
        layoutProgressSection = findViewById(R.id.layoutProgressSection);
        btnConfirmRecieved = findViewById(R.id.btnConfirmReceived);

        if(!order.status.equals("Arrived")) {
            btnConfirmRecieved.setEnabled(false);
        }
        btnConfirmRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderDetailsIntent = new Intent(OrderDetailsActivity.this,
                                                        CustomerRecieveActivity.class);
                startActivityForResult(orderDetailsIntent, customerQRCodeRequest);
            }
        });

        txtOrderId.setText("#" + order.id.toUpperCase());
        txtAmount.setText("$" + df2.format(order.price));
        txtFlightNumber.setText(order.flightNumber);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String qrcodeText = data.getStringExtra("qrcode");
                Log.d(TAG, qrcodeText);

                // TODO: Check if qrcode is valid and save state
                if (txtOrderCollected != null &&
                        layoutProgressSection != null &&
                        btnConfirmRecieved != null) {

                    layoutProgressSection.setVisibility(View.GONE);
                    btnConfirmRecieved.setVisibility(View.GONE);
                    txtOrderCollected.setVisibility(View.VISIBLE);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
