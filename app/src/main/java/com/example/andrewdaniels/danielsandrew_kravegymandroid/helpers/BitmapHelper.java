package com.example.andrewdaniels.danielsandrew_kravegymandroid.helpers;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

public class BitmapHelper {

//    private static Size getScaledSize(Size original, int minWidth, int minHeight) {
//        if (minHeight > 0 && minWidth > 0) {
//            int width = original.getWidth();
//            int height = original.getHeight();
//
//            float ratioBitmap = (float) width / (float) height;
//            float ratioMin = (float) minWidth / (float) minHeight;
//
//            int finalWidth = minWidth;
//            int finalHeight = minHeight;
//
//            if (ratioMin < ratioBitmap) {
//                finalWidth = (int) ((float) minWidth * ratioBitmap);
//            } else {
//                finalHeight = (int) ((float) minHeight / ratioBitmap);
//            }
//            return new Size(finalWidth, finalHeight);
//        } else {
//            return null;
//        }
//    }

    public static Bitmap scaleBitmapAndKeepRatio(Bitmap targetBmp, int reqHeightInPixels, int reqWidthInPixels)
    {
        Matrix matrix = new Matrix();
        matrix .setRectToRect(new RectF(0, 0, targetBmp.getWidth(), targetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmp.getWidth(), targetBmp.getHeight(), matrix, true);
        targetBmp.recycle();
        return scaledBitmap;
    }
}
