package com.example.better_together.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.example.better_together.storage.SharedPrefStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ssdd on 11/9/14.
 */
public class FileHelper {
    private static final String TAG = FileHelper.class.getName();
    private static volatile boolean sIsInitiated = false;
    private static Activity sContext;

    public enum FileFormat{
        JPEG,
        PNG
    }

    public static void setActivityContext(Activity activityContext){
        sContext = activityContext;
    }

    public static boolean writeFileToDirectory(Bitmap picBitmap,String dirPath,String fileName,FileFormat fileFormat,int mode){
        String fileFormatPostFix = "";
        Bitmap.CompressFormat compressFormat = null;
        switch (fileFormat){
            case JPEG:{
                fileFormatPostFix = ".jpg";
                compressFormat = Bitmap.CompressFormat.JPEG;
                break;
            }case PNG:{
                fileFormatPostFix = ".png";
                compressFormat = Bitmap.CompressFormat.PNG;
                break;
            }
        }

        boolean ret = false;
        File directory = sContext.getDir(dirPath,mode);
        File picFile = new File(directory,fileName + fileFormatPostFix);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(picFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            picBitmap.compress(compressFormat, 100, fos);
            fos.close();
            ret = true;
        }catch(FileNotFoundException e){
            Log.e(TAG,"cannot find file",e);
            ret = false;
        }catch(IOException e){
            Log.e(TAG,"ioexception",e);
            ret = false;
        }
        return ret;
    }
}
