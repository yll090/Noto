package com.accedia.noto;

import android.app.Application;

import com.accedia.noto.dagger.AppModule;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {

    public TestAppModule(Application application) {
        super(application);
    }
}
