package vn.tungdx.mediapicker;

import android.app.Application;

public class MediaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.MediaPickerTheme);
    }
}
