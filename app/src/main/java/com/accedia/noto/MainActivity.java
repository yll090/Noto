package com.accedia.noto;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.accedia.noto.dagger.Dagger;
import com.accedia.noto.helpers.AppExecutors;
import com.accedia.noto.model.Word;
import com.accedia.noto.viewModels.AppViewModelFactory;
import com.accedia.noto.viewModels.WordViewModel;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1337;
    private WordViewModel wordViewModel;
    @Inject
    AppViewModelFactory factory;
    @Inject
    AppExecutors appExecutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Dagger.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WordAdapter adapter = getWordAdapter();
        wordViewModel = ViewModelProviders.of(this, factory).get(WordViewModel.class);
        observe(adapter);
        setupFAB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            wordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void observe(final WordAdapter adapter) {
        wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                adapter.setWords(words);
            }
        });
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_add_word);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }


    @NonNull
    private WordAdapter getWordAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordAdapter adapter = new WordAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }
}
