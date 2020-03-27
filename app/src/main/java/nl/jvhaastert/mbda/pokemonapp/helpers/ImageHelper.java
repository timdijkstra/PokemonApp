package nl.jvhaastert.mbda.pokemonapp.helpers;

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
        //The getExternalStorageDirectory is deprecated in android Q
        //So we use the MediaStore approach here.
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
                    Toast.makeText(activity, "Afbeelding opgeslagen!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, "Er ging iets mis bij het opslaan. Probeer het opnieuw!",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //For all older versions we use the old method as the above approach
        //Does not work on Android 8.
        else {
            FileOutputStream outStream;
            File sdCard = Environment.getExternalStorageDirectory();
            dir = new File(sdCard.getAbsolutePath() + DIRECTORY);
            dir.mkdirs();

            File outFile = new File(dir, fileName);
            try {
                outStream = new FileOutputStream(outFile);
                saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

                if(saved) {
                    Toast.makeText(activity, "Afbeelding opgeslagen!",
                            Toast.LENGTH_SHORT).show();

                    //Refresh gallery after saving
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    activity.sendBroadcast(intent);
                }else {
                    Toast.makeText(activity, "Er ging iets mis bij het opslaan. Probeer het opnieuw!",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                Log.e("PokemonActivity", e.getMessage(), e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
