package com.accedia.noto;

import android.app.Application;
import android.os.StrictMode;

import com.accedia.noto.dagger.Dagger;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll() //for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        } else {
            Dagger.init(this);
        }

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mNetComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

}