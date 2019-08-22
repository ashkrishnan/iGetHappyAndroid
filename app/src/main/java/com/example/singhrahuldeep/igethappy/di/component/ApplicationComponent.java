package com.example.singhrahuldeep.igethappy.di.component;

import android.app.Application;
import android.content.Context;

import com.example.singhrahuldeep.igethappy.App;
import com.example.singhrahuldeep.igethappy.di.ApplicationContext;
import com.example.singhrahuldeep.igethappy.di.module.ApplicationModule;
import com.example.singhrahuldeep.igethappy.utils.Utils;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();

    Utils getUtils();

    void inject(App app);


}
