package com.accedia.noto.dagger;

import com.accedia.noto.MainActivity;
import com.accedia.noto.viewModels.NewsViewModel;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent extends ApplicationComponent {

    void inject(MainActivity mainActivity);

    void inject(NewsViewModel newsViewModel);

}
