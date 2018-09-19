package com.accedia.noto.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.accedia.noto.db.Database;
import com.accedia.noto.db.WordDAO;
import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.model.Word;

import java.util.List;

public class WordRepo {

    private final AppExecutors appExecutors;
    private WordDAO wordDAO;
    private LiveData<List<Word>> allWords;
    private boolean inMemoryDb = false;

    public WordRepo(Application application, AppExecutors appExecutors, boolean inMemoryDb) {
        Database db = Database.getDatabase(application, inMemoryDb);
        this.inMemoryDb = inMemoryDb;
        this.appExecutors = appExecutors;
        wordDAO = db.wordDAO();
        allWords = wordDAO.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        if (inMemoryDb) {
            wordDAO.insert(word);
        } else {
            appExecutors.getDiskIO().execute(() -> {
                wordDAO.insert(word);
            });
        }
//        new insertAsyncTask(wordDAO).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDAO daoTask;

        insertAsyncTask(WordDAO dao) {
            daoTask = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            daoTask.insert(params[0]);
            return null;
        }
    }

}
