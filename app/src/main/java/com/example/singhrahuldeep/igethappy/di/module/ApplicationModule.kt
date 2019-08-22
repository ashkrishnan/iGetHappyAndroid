package com.example.singhrahuldeep.igethappy.di.module

import android.app.Application
import android.content.Context
import com.example.singhrahuldeep.igethappy.di.ApplicationContext
import com.example.singhrahuldeep.igethappy.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideDateUtil(): Utils {
        return Utils()
    }


}
