package ai.rt5k.krisshop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.HashMap;
import java.util.Map;

import ai.rt5k.krisshop.ModelObjects.Order;

public class EmConfirmActivity extends AppCompatActivity {
    MainApplication m;
    Order order;

    private QRCodeReaderView qrcodeReaderView;
    private Button btnCancel;
    private static final int PERMISSIONS_REQUEST_CAMERA = 0;
    private static final String TAG = "EmConfirmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = (MainApplication) getApplicationContext();
        order = (Order) getIntent().getBundleExtra("orderBundle").getSerializable("order");

        getSupportActionBar().setTitle("Confirm Packing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check for camera permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);

        } else {
            setContentView(R.layout.activity_qr_scanner);
            setQRReader();

            btnCancel = findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, start qrcode reader
                    Log.d(TAG, "Permission granted");
                    setContentView(R.layout.activity_qr_scanner);
                    setQRReader();
                    qrcodeReaderView.startCamera();

                } else {
                    // permission denied, go back to previous activity
                    Log.d(TAG, "Permission denied");
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                return;
            }
        }
    }

    private void setQRReader() {
        qrcodeReaderView = findViewById(R.id.qrDecoderView);
        qrcodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                qrcodeReaderView.stopCamera();
                AlertDialog.Builder builder = new AlertDialog.Builder(EmConfirmActivity.this);
                builder.setTitle("Confirm Packing").setMessage("Would you like to confirm that this order has been packed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StringRequest confirmRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/update_order", new Response.Listener<String>() {
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
                                        params.put("set_to", "Arrived");
                                        return params;
                                    }
                                };
                                m.mainQueue.add(confirmRequest);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));

                    }
                });

                dialog.show();
            }
        });
        qrcodeReaderView.setQRDecodingEnabled(true);
        qrcodeReaderView.setBackCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (qrcodeReaderView != null) {
            qrcodeReaderView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (qrcodeReaderView != null) {
            qrcodeReaderView.stopCamera();
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
