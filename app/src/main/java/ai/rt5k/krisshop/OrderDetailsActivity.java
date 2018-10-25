package ai.rt5k.krisshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ai.rt5k.krisshop.ModelObjects.Order;

public class OrderDetailsActivity extends AppCompatActivity {
    MainApplication m;

    Order order;
    Button btnConfirmRecieved, btnCancelReport;
    TextView txtOrderId, txtAmount, txtFlightNumber;
    TextView txtOrderCollected;
    RelativeLayout layoutProgressSection;
    LinearLayout lytReport, lytButtons;
    ProgressBar prgStatus;
    ImageView checkpointPacked, checkpointOnboard, checkpointArrived, imgPhoto;
    Spinner spnReportReason;
    Button btnCancel, btnSubmitReport;
    EditText edtMessage;

    private static final int customerQRCodeRequest = 1;
    private static final String TAG = "OrderDetailsActivity";
    private static DecimalFormat df2 = new DecimalFormat(".00");
    boolean isImageFitToScreen = false;

    private static final String[] REASONS = new String[] {
            "Check order status",
            "Package missing",
            "Package damage",
            "Others"
    };
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

        m = (MainApplication) getApplicationContext();
        order = (Order) getIntent().getBundleExtra("orderBundle").getSerializable("order");

        txtOrderId = findViewById(R.id.txt_order_id);
        txtAmount = findViewById(R.id.txt_amount);
        txtFlightNumber = findViewById(R.id.txt_flight_number);

        txtOrderCollected = findViewById(R.id.txtOrderCollected);
        layoutProgressSection = findViewById(R.id.layoutProgressSection);
        btnConfirmRecieved = findViewById(R.id.btnConfirmReceived);
        btnCancelReport = findViewById(R.id.btnCancelReport);

        lytButtons = findViewById(R.id.lytButtons);
        lytReport = findViewById(R.id.lytReport);

        spnReportReason = findViewById(R.id.spnReportReason);
        edtMessage = findViewById(R.id.edtMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderDetailsActivity.this, R.layout.spinner_item, REASONS);
        spnReportReason.setAdapter(adapter);

        if(!order.status.equals("Arrived")) {
            btnConfirmRecieved.setEnabled(false);
        }
        if(!order.status.equals("Ordered")) {
            btnCancelReport.setText("Report");
        }

        btnConfirmRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderDetailsIntent = new Intent(OrderDetailsActivity.this,
                                                        CustomerRecieveActivity.class);
                startActivityForResult(orderDetailsIntent, customerQRCodeRequest);
            }
        });

        btnCancelReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.status.equals("Ordered")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                    final AlertDialog dialog = builder.setTitle("Cancel Order")
                            .setMessage("Are you sure you would like to cancel this order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StringRequest cancelRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/update_order", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("SUCCESS")) {
                                                order.status = "Arrived";
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                            Log.d(TAG, response);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("uid", m.sessionId);
                                            params.put("order_id", order.id);
                                            params.put("set_to", "Cancelled");
                                            return params;
                                        }
                                    };

                                    m.mainQueue.add(cancelRequest);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setCancelable(false).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));

                        }
                    });
                    dialog.show();
                }
                else {
                    lytButtons.setVisibility(View.GONE);
                    lytReport.setVisibility(View.VISIBLE);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytButtons.setVisibility(View.VISIBLE);
                lytReport.setVisibility(View.GONE);
            }
        });

        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = edtMessage.getText().toString();

                if(message.isEmpty()) {
                    Snackbar.make(edtMessage, "Please enter a description of your issue", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                StringRequest reportRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/report_issue/" + order.id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("SUCCESS")) {
                            Snackbar.make(edtMessage, "Report for order #" + order.id.toUpperCase() + " submitted", Snackbar.LENGTH_SHORT).show();
                            lytButtons.setVisibility(View.VISIBLE);
                            lytReport.setVisibility(View.GONE);
                            edtMessage.setText("");
                            spnReportReason.setSelection(0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("issue", REASONS[spnReportReason.getSelectedItemPosition()]);
                        params.put("message", message);
                        return params;
                    }
                };
                m.mainQueue.add(reportRequest);
            }
        });

        txtOrderId.setText("#" + order.id.toUpperCase());
        txtAmount.setText("$" + df2.format(order.price));
        txtFlightNumber.setText(order.flightNumber);

        prgStatus = findViewById(R.id.prgStatus);
        checkpointPacked = findViewById(R.id.checkpointPacked);
        checkpointOnboard = findViewById(R.id.checkpointOnboard);
        checkpointArrived = findViewById(R.id.checkpointArrived);
        imgPhoto = findViewById(R.id.imgPhoto);

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewImageIntent = new Intent(OrderDetailsActivity.this, CustomerViewImageActivity.class);
                startActivity(viewImageIntent);
            }
        });
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

        if(!order.status.equals("Arrived")) {
            btnConfirmRecieved.setEnabled(false);
        }

        if(order.status.equals("Arrived")) {
            imgPhoto.setVisibility(View.VISIBLE);
            btnConfirmRecieved.setEnabled(true);
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
