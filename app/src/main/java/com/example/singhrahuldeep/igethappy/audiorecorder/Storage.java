package com.example.singhrahuldeep.igethappy.audiorecorder;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;


import java.io.*;
import java.text.SimpleDateFormat;

public class Storage {
    public static final String TMP_REC = "recorind.data";
    public static final String RECORDINGS = "IgetHappy";
    public static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public static String getNameNoExt(File f) {
        String fileName = f.getName();

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileName = fileName.substring(0, i);
        }
        return fileName;
    }

    public static String getExt(File f) {
        String fileName = f.getName();

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        }
        return "";
    }

    public boolean permitted(String[] ss) {
        for (String s : ss) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public File getLocalStorage() {
        return new File(Environment.getExternalStorageDirectory(), RECORDINGS);
    }

    public boolean isLocalStorageEmpty() {
        return getLocalStorage().listFiles().length == 0;
    }

    public boolean isExternalStoragePermitted() {
        return permitted(PERMISSIONS);
    }

    public boolean recordingPending() {
        return getTempRecording().exists();
    }

    File getNextFile(File parent, File f) {
        String fileName = f.getName();

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
            fileName = fileName.substring(0, i);
        }

        return getNextFile(parent, fileName, extension);
    }

    File getNextFile(File parent, String name, String ext) {
        String fileName;
        if (ext.isEmpty())
            fileName = name;
        else
            fileName = String.format("%s.%s", name, ext);

        File file = new File(parent, fileName);

      /*  int i = 1;
        while (file.exists()) {
            fileName = String.format("%s (%d).%s", name, i, ext);
            file = new File(parent, fileName);
            i++;
        }*/

//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to create: " + file, e);
//        }

        return file;
    }


    public File getNewFile() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        String ext = "wav";

        File parent = getStoragePath();
        if (!parent.exists()) {
            if (!parent.mkdirs())
                throw new RuntimeException("Unable to create: " + parent);
        }

        return getNextFile(parent, "recording", ext);
    }

    public File getStoragePath() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        String path = "Audio Recorder";
        if (permitted(PERMISSIONS)) {
            return getLocalStorage();
        } else {
            return getLocalStorage();
        }
    }

    public long getFree(File f) {
        while (!f.exists())
            f = f.getParentFile();

        StatFs fsi = new StatFs(f.getPath());
        if (Build.VERSION.SDK_INT < 18)
            return fsi.getBlockSize() * fsi.getAvailableBlocks();
        else
            return fsi.getBlockSizeLong() * fsi.getAvailableBlocksLong();
    }

    public File getTempRecording() {
        File internal = new File(Environment.getExternalStorageDirectory(), TMP_REC);

        if (internal.exists())
            return internal;

        // Starting in KITKAT, no permissions are required to read or write to the returned path;
        // it's always accessible to the calling app.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (!permitted(PERMISSIONS))
                return internal;
        }

        File external = new File(context.getExternalCacheDir(), TMP_REC);

        if (external.exists())
            return external;

        long freeI = getFree(internal);
        long freeE = getFree(external);

        if (freeI > freeE)
            return internal;
        else
            return external;
    }

    public FileOutputStream open(File f) {
        File tmp = f;
        File parent = tmp.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new RuntimeException("unable to create: " + parent);
        }
        if (!parent.isDirectory())
            throw new RuntimeException("target is not a dir: " + parent);
        try {
            return new FileOutputStream(tmp, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(File f) {
        f.delete();
    }

    public void move(File f, File to) {
        try {
            InputStream in = new FileInputStream(f);
            OutputStream out = new FileOutputStream(to);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            f.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void migrateLocalStorage() {
        if (!permitted(PERMISSIONS)) {
            return;
        }
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        String path = "/storage/emulated/0/iGetHappy";
        File l = getLocalStorage();
        File t = new File(path);
        t.mkdirs();

        File[] ff = l.listFiles();

        if (ff == null)
            return;

        for (File f : ff) {
            File tt = getNextFile(t, f);
            move(f, tt);
        }
    }

}
