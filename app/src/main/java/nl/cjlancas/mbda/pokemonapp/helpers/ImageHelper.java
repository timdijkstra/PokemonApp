package nl.cjlancas.mbda.pokemonapp.helpers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageHelper {

    private static final String DIRECTORY = "/pokemon";

    public void saveImageToFile(ImageView image, Activity activity) {

        BitmapDrawable draw = (BitmapDrawable) image.getDrawable();

        Bitmap bitmap = draw.getBitmap();

        OutputStream outputStream;

        String fileName = String.format("%d.jpg", System.currentTimeMillis());

        boolean saved;

        File dir;

        //mediastore used due to getexternalstoragedirectory being deprecated in AndroidQ

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + DIRECTORY);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                outputStream = resolver.openOutputStream(imageUri);
                saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                //Refresh gallery after saving
                dir = new File(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + DIRECTORY);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(dir));
                activity.sendBroadcast(intent);

                if(saved) {
                    Toast.makeText(activity, "Image saved.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
