package com.uidmadapp.urgenceidentification;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by nyaina on 17/04/2016.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    boolean rounded;
    int targetW;

    public DownloadImageTask(ImageView bmImage, boolean rounded) {
        this.bmImage = bmImage;
        this.rounded = rounded;
        targetW = bmImage.getWidth();
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
//            Log.i("INFORMATION : ",urldisplay);
            InputStream in = new java.net.URL(urldisplay).openStream();

            Bitmap tmp = BitmapFactory.decodeStream(in);
            int photoW = tmp.getWidth();
            int scalefactor = 1;
            //if (photoW > 0 && targetW > 0)
              //  scalefactor = photoW / targetW;
            //if (scalefactor == 0) scalefactor = 1;
            int scaleHeight = (int) (tmp.getHeight() / scalefactor);
            int scaleWidth = (int) (tmp.getWidth() / scalefactor);
            mIcon11 = Bitmap.createScaledBitmap(tmp, scaleWidth, scaleHeight, true);
        } catch (Exception e) {
            Log.e("DownloadTask Erro", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (rounded)
            bmImage.setImageBitmap(AssetsTool.getCircleBitmap(result));
        else
            bmImage.setImageBitmap(result);
    }
}
