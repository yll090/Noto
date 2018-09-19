package com.accedia.noto.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.accedia.noto.model.Word;
import com.accedia.noto.repository.WordRepo;

import java.util.List;

import javax.inject.Inject;

public class WordViewModel extends AndroidViewModel {

    private final LiveData<List<Word>> allWords;

    private WordRepo repo;

    public WordViewModel(@NonNull Application application, WordRepo repo) {
        super(application);
        this.repo = repo;
        this.allWords = repo.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        repo.insert(word);
    }
}
