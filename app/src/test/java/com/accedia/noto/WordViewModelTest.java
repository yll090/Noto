package com.accedia.noto;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Looper;

import com.accedia.noto.db.Database;
import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.model.Word;
import com.accedia.noto.repository.WordRepo;
import com.accedia.noto.viewModels.WordViewModel;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.List;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
@PrepareForTest({Looper.class, Database.class})
public class WordViewModelTest {

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    App app;

    @Mock
    WordViewModel wordViewModel;

    AppExecutors appExecutors;

    @BeforeClass
    public static void setUp() {
        mockStatic(Looper.class);
        mockStatic(Database.class);
        Looper mockLooper = mock(Looper.class);
        when(mockLooper.getThread()).thenReturn(mock(Thread.class));
        when(Looper.getMainLooper()).thenReturn(mockLooper);
    }

    @Test
    public void testWordViewModel() {
        System.out.println("we test nao");
        appExecutors = new AppExecutors(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3));
        System.out.println("appExecutors fine");
        when(app.getApplicationContext()).thenReturn(mock(Context.class));
        System.out.println("mocked the getApplicationContext");
        WordRepo wordRepo = new WordRepo(app, appExecutors, true);
        Word expectedWord = new Word("test");
        mock(WordRepo.class).insert(expectedWord);
        wordRepo.insert(expectedWord);

        WordViewModel wordViewModel = new WordViewModel(app, wordRepo);

        LifecycleRegistry lifecycle = new LifecycleRegistry(mock(LifecycleOwner.class));
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        LiveData<List<Word>> results = wordViewModel.getAllWords();

        verify(results.getValue().size()).equals(1);
        verify(results.getValue().get(0)).equals(expectedWord);

    }
}
