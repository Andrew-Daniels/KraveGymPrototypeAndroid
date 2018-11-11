package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.example.andrewdaniels.danielsandrew_kravegymandroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapHelper {

    public static Bitmap scaleBitmapAndKeepRatio(Bitmap targetBmp, int reqHeightInPixels, int reqWidthInPixels)
    {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, targetBmp.getWidth(), targetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmp.getWidth(), targetBmp.getHeight(), matrix, true);
        targetBmp.recycle();
        return scaledBitmap;
        //return saveImage(c, username, scaledBitmap);
    }

    private static Uri saveImage(Context c, String username, Bitmap image) {
        File localStorage = c.getFilesDir();

        FileOutputStream outputStream;

        File imageToSave = new File(localStorage, username);

        try {
            outputStream = c.openFileOutput(username, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            String authority = c.getResources().getString(R.string.fileprovider_authority);
            return FileProvider.getUriForFile(c, authority, imageToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Uri loadImage(Context c, String username) {
        File localStorage = c.getFilesDir();
        String authority = c.getResources().getString(R.string.fileprovider_authority);
        File imageToLoad = new File(localStorage, username);
        return FileProvider.getUriForFile(c, authority, imageToLoad);
    }
}
