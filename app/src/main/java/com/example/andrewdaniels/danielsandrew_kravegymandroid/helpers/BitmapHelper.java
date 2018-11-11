package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.example.andrewdaniels.danielsandrew_kravegymandroid.interfaces.FirebaseCallback;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapHelper {

    private static final String TAG = "BitmapHelper.TAG";

    public static boolean scaleBitmapAndKeepRatio(FirebaseCallback callback, String username, Bitmap fullsizeImage, int reqHeightInPixels, int reqWidthInPixels)
    {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, fullsizeImage.getWidth(), fullsizeImage.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(fullsizeImage, 0, 0, fullsizeImage.getWidth(), fullsizeImage.getHeight(), matrix, true);
        fullsizeImage.recycle();
        //TODO: Return Uri here
        Context c = callback.getCallbackContext();
        return saveImage(c, username, scaledBitmap);
    }

    private static boolean saveImage(Context c, String username, Bitmap image) {
        File localStorage = c.getFilesDir();

        FileOutputStream outputStream;

        File imageToSave = new File(localStorage, username);
        if (imageToSave.exists()) {return true;}

        try {
            outputStream = c.openFileOutput(username, Context.MODE_PRIVATE);
            boolean imageSaved = image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            image.recycle();
            return imageSaved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap loadImage(Context c, String username) {
        File localStorage = c.getFilesDir();
        File imageToLoad = new File(localStorage, username);
        String filePath = imageToLoad.getAbsolutePath();

        return BitmapFactory.decodeFile(filePath, null);
    }
}
