package com.accedia.noto.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

public class Dagger {

    private static Dagger instance;
    private static ApplicationComponent appComponent;

    private Dagger() {

    }

    public static Dagger getInstance() {
        if (instance == null) {
            instance = new Dagger();
        }
        return instance;
    }

    public static void init(@NonNull Application application) {
        String baseURL = "https://newsapi.org";
        appComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(application)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule(baseURL))
                .build();
    }

    public AppComponent getAppComponent() {
        return (AppComponent) appComponent;
    }
}
