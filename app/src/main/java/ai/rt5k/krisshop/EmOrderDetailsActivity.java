package ai.rt5k.krisshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ai.rt5k.krisshop.ModelObjects.Order;

public class EmOrderDetailsActivity extends AppCompatActivity {
    private static final int EMPLOYEE_QRCODE = 0;

    Order order;
    Button btnConfirmRecieved;
    TextView txtItemName, txtOrderId, txtFlightNumber;
    TextView txtOrderCollected;
    RelativeLayout layoutProgressSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = (Order) getIntent().getBundleExtra("orderBundle").getSerializable("order");

        getSupportActionBar().setTitle("Order " + order.id);

        txtItemName = findViewById(R.id.txt_item_name);
        txtOrderId = findViewById(R.id.txt_order_id);
        txtFlightNumber = findViewById(R.id.txt_flight_number);

        txtOrderCollected = findViewById(R.id.txtOrderCollected);
        layoutProgressSection = findViewById(R.id.layoutProgressSection);
        btnConfirmRecieved = findViewById(R.id.btnConfirmReceived);

        btnConfirmRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderDetailsIntent = new Intent(EmOrderDetailsActivity.this, CustomerRecieveActivity.class);
                startActivityForResult(orderDetailsIntent, EMPLOYEE_QRCODE);
            }
        });

        txtItemName.setText(order.name);
        txtOrderId.setText(order.id);
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
}
