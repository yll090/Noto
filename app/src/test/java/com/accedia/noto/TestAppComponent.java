
package com.accedia.noto;

import com.accedia.noto.dagger.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TestAppModule.class)
public interface TestAppComponent /*extends AppComponent*/ {
    void inject(WordViewModelTest test);
}

