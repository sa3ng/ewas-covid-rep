package com.mp3.ewas_covid_app.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mp3.ewas_covid_app.Models.Transaction;
import com.mp3.ewas_covid_app.R;

import java.util.ArrayList;

public class Helper {

//    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Bitmap generateBitmap(String s, Context context) {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, 300, 300, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];

            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? context.getColor(R.color.black) : context.getColor(R.color.white);
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();

            return bitmap;
        }
    }

    public static double reduceCreds(Double oldCredit, Double toReduce){
        Double result = oldCredit - toReduce;

        //TODO: set try catch for amount to reduce greater than old credit
        return result;
    }

    public static double addCreds(Double oldCredit, Double toAdd){
        Double result = oldCredit + toAdd;
        return  result;
    }

    public static void setSampleUserInfo(ArrayList<Transaction> orgArrayList) {
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));
        orgArrayList.add(new Transaction("Edgar", "January 6, 2022", "2:00 am"));

    }

}
