package ai.rt5k.krisshop.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageThread extends Thread {
    String url;
    ImageView view;

    public ImageThread(String url, ImageView view) {
        this.url = url;
        this.view = view;
    }

    public void run() {
        try {
            final URL url = new URL(this.url);
            final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(bmp);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
