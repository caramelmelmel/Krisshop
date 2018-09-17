package ai.rt5k.krisshop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class CustomerRecieveActivity extends AppCompatActivity {

    private QRCodeReaderView qrcodeReaderView;
    private Button btnCancel;
    private static final int PERMISSIONS_REQUEST_CAMERA = 0;
    private static final String TAG = "CustomerRecieveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

                // TODO: Save qrcode text
                qrcodeReaderView.stopCamera();
                RateDialogFragment rateDialog = new RateDialogFragment();
                rateDialog.show(getSupportFragmentManager(), "RateDialogFragment");
                rateDialog.setCancelable(false);
                rateDialog.setQrcodeText(text);
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
}


