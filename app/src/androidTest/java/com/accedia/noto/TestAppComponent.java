package com.accedia.noto;

import com.accedia.noto.dagger.ApplicationComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TestAppModule.class)
public interface TestAppComponent extends ApplicationComponent {
    void inject(UITests test);
}

