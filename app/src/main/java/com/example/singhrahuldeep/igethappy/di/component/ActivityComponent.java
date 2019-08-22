package com.example.singhrahuldeep.igethappy.di.component;


import com.example.singhrahuldeep.igethappy.di.PerActivity;
import com.example.singhrahuldeep.igethappy.di.module.ActivityModule;
import com.example.singhrahuldeep.igethappy.views.auth.MainActivity;
import dagger.Component;
import org.jetbrains.annotations.NotNull;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {


    void inject(@NotNull MainActivity mainActivity);
}
