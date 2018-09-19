package com.accedia.noto;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.accedia.noto.db.Database;
import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.model.Word;
import com.accedia.noto.repository.WordRepo;
import com.accedia.noto.viewModels.WordViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class WordViewModelInstTest {

//    @Rule
//    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    Database db;
    private AppExecutors appExecutors;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, Database.class).build();

    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void dbWorx() {
        Word word = new Word("FFFFFFFFFFF");
        db.wordDAO().insert(word);
        List<Word> words = db.wordDAO().getAllWordsTesting();

        assertThat(words.size(), equalTo(1));
        assertThat(words.get(0).getWord(), equalTo(word.getWord()));
    }

    @Test
    public void testWordViewModel() throws InterruptedException {
        appExecutors = new AppExecutors(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3));

        Context context = InstrumentationRegistry.getTargetContext();
        Application app = (Application) context.getApplicationContext();

        WordRepo wordRepo = new WordRepo(app, appExecutors, true);
        Word expectedWord = new Word("test");
        wordRepo.insert(expectedWord);

        WordViewModel wordViewModel = new WordViewModel(app, wordRepo);

        LifecycleRegistry lifecycle = new LifecycleRegistry(mock(LifecycleOwner.class));
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        LiveData<List<Word>> results = wordViewModel.getAllWords();

        List<Word> words = LiveDataTestUtil.getValue(results);
        assertThat(words.size(), equalTo(1));
        assertThat(words.get(0).getWord(), equalTo(expectedWord.getWord()));

    }

}
