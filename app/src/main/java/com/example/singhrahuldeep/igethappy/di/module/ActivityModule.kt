package com.example.singhrahuldeep.igethappy.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import com.example.singhrahuldeep.igethappy.di.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val mActivity: AppCompatActivity) {

    @Provides
    @ActivityContext
    internal fun provideContext(): Context {
        return mActivity
    }

    @Provides
    internal fun provideActivity(): AppCompatActivity {
        return mActivity
    }

}
