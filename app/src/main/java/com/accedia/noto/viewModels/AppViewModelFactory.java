package com.accedia.noto.viewModels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.accedia.noto.repository.WordRepo;

public class AppViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private WordRepo repo;

    public AppViewModelFactory(Application application, WordRepo repo) {
        this.application = application;
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(WordViewModel.class)) {
            return (T) new WordViewModel(application, repo);
        }
        return null;
    }
}
