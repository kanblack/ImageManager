package imageloader;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class Utils {
   public static void CopyStream(InputStream is, OutputStream os)
   {
       final int buffer_size=1024;
       try
       {
           byte[] bytes=new byte[buffer_size];
           for(;;)
           {
             int count=is.read(bytes, 0, buffer_size);
             if(count==-1)
                 break;
             os.write(bytes, 0, count);
           }
       }
       catch(Exception ex){}
   }
   
   public static Bitmap getBitmapFromUrl(String imgUrl)
   {
		try {
			URL url = new URL(imgUrl);
			Bitmap bmp; 
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			  return bmp;
		} catch (MalformedURLException e) {
//			Log.e("haimt", e.getMessage(), e);
//			return Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
			return null;
		} catch (IOException e) {
//			Log.e("haimt", e.getMessage(), e);
//			return Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
			return null;
		} catch (Exception e) {
			Log.e("haimt", e.getMessage(), e);
//			return Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
			return null;
		}
   }
   
   public static Bitmap resizeImage(Bitmap bitmap, int height, int width){
	   if(bitmap == null) {
//		   return Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
		   return null;
	   }
	    int oldWidth = bitmap.getWidth();
	    int oldHeight = bitmap.getHeight(); 

	    float scaleWidth = ((float) width) / oldWidth;
	    float scaleHeight = ((float) height) / oldHeight;
	    
	    if(scaleWidth == 1.0) {
	    	return bitmap;
	    }

	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);

	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, 
	            oldWidth, oldHeight, matrix, true); 

	    return resizedBitmap;
	}

	public final static int DEFAULT_WIDTH = 140;
	public final static int DEFAULT_HEIGHT = 160;
}
