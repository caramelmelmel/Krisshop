package ai.rt5k.krisshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class EmCameraActivity extends AppCompatActivity{

    Camera mCamera;
    boolean mPreviewRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Setup Camera
        SurfaceView surfaceCamera = findViewById(R.id.surfaceCamera);
        SurfaceHolder surfaceCameraHolder = surfaceCamera.getHolder();
        surfaceCameraHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mCamera = Camera.open();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                if (mPreviewRunning) {
                    mCamera.stopPreview();
                }

                Camera.Parameters p = mCamera.getParameters();
                p.setPreviewSize(w, h);
                mCamera.setParameters(p);

                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mCamera.startPreview();
                mPreviewRunning = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mCamera.stopPreview();
                mPreviewRunning = false;
                mCamera.release();
            }
        });
        surfaceCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // Setup Camera Listeners
        final Camera.PictureCallback jpgPictureCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] imageData, Camera c) {
                mCamera.startPreview();
                mPreviewRunning = true;
            }
        };

        ImageView imgTakePhoto = findViewById(R.id.imgTakePhoto);
        imgTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPreviewRunning) {
                    mCamera.takePicture(null, null, jpgPictureCallback);
                    mPreviewRunning = false;
                }
            }
        });

    }
}
