package com.juslt.common.utils;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zl on 2017/5/19.
 */

public class BitmapUtil {

    public static void saveBitmap2file(byte[] buf, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (file.exists())
                file.delete();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存bitmap到硬盘上
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String filename) {
        return saveBitmapToFile(bitmap, filename, 100);
    }

    /**
     * 保存bitmap到硬盘上,
     *
     * @param quality
     *            100为不压缩
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String filename,
                                           int quality) {
        String suffix = filename.substring(filename.lastIndexOf("."))
                .toLowerCase();
        Bitmap.CompressFormat format = null;
        if (suffix.equals(".png")) {
            format = Bitmap.CompressFormat.PNG;
        } else {
            format = Bitmap.CompressFormat.JPEG;
        }

        OutputStream os = null;
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            os = new FileOutputStream(file);
            return bitmap.compress(format, quality, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}
