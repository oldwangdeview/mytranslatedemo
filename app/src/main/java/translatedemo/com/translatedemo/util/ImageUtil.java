package translatedemo.com.translatedemo.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by oldwang on 2019/1/15 0015.
 */

public class ImageUtil {
    /**
     * 保存bitmap到本地
     * @param bitmap
     * @return
     */
    public static void saveBitmap(Bitmap bitmap, String savePath) {
        File filePic;
        try {
            filePic = new File(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePic));
            //注意Bitmap.CompressFormat.PNG
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取相册图片路径
     * @param context
     * @param data
     * @return
     */
    public static String selectImage(Context context, Intent data){
        String path;
        if (data != null) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA },null, null, null);
                if (null == cursor) {
                    Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
                    return null;
                }
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            } else {
                path = uri.getPath();
            }
        }else{
            Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
            return null;
        }
        return path;
    }

}
