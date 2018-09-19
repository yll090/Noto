package com.accedia.noto.dagger;

import android.app.Application;

import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.repository.WordRepo;
import com.accedia.noto.viewModels.AppViewModelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final ExecutorService diskIO;
    private final ExecutorService networkIO;
    Application application;
    WordRepo wordRepo;
    AppViewModelFactory factory;

    AppExecutors appExecutors;

    public AppModule(Application application) {
        this.application = application;
        diskIO = Executors.newSingleThreadExecutor();
        networkIO = Executors.newFixedThreadPool(3);
        appExecutors = new AppExecutors(diskIO, networkIO);
        this.wordRepo = new WordRepo(this.application, appExecutors, false);
        factory = new AppViewModelFactory(this.application, wordRepo);
    }

    @Provides
    @Singleton
    AppExecutors providesAppExecutors() {
        return appExecutors;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    WordRepo providesWordRepo() {
        return wordRepo;
    }

    @Provides
    @Singleton
    AppViewModelFactory providesAppViewModelFactory() {
        return factory;
    }
}
