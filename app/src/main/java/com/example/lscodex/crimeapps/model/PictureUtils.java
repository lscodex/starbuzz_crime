package com.example.lscodex.crimeapps.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

/**
 * Created by lscodex on 7.11.2017.
 */

public class PictureUtils {

    public static Bitmap getScaledBitMap(String path,int destWidth,int destHeight){

        // read from disk photo

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;


        // calculate photo measure

        int inSampleSize = 1; // key paramater
        if(srcHeight>destHeight || srcWidth>destWidth){
            float heightScale = srcHeight/destHeight;
            float widthScale = srcWidth/destWidth;
            inSampleSize = Math.round(heightScale>widthScale ? heightScale: widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // read and create bitmap for photo
        return BitmapFactory.decodeFile(path,options);
    }

    // measure photo for screen
    public static Bitmap getScaledBitMap (String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitMap(path,size.x,size.y);
    }

    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable)){
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
        bitmapDrawable.getBitmap().recycle();
        imageView.setImageBitmap(null);
    }
}
