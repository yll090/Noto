package com.accedia.noto.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.accedia.noto.model.Word;

import java.util.List;

import static com.accedia.noto.model.Word.TABLE_WORD;
import static com.accedia.noto.model.Word.WORD_TABLE_WORD;


@Dao
public interface WordDAO {

    @Insert
    void insert(Word word);

    @Query("DELETE from " + TABLE_WORD)
    void deleteAll();

    @Query("SELECT * from " + TABLE_WORD + " ORDER BY " + WORD_TABLE_WORD)
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * from " + TABLE_WORD + " ORDER BY " + WORD_TABLE_WORD)
    List<Word> getAllWordsTesting();

}