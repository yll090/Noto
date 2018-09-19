package com.accedia.noto.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static com.accedia.noto.model.Word.TABLE_WORD;

@Entity(tableName = TABLE_WORD)
public class Word {

    public static final String TABLE_WORD = "word_table";
    public static final String WORD_TABLE_WORD = "word";

    @PrimaryKey
    @NonNull
    //    @ColumnInfo(name = "word")
    private String word;

    public Word(@NonNull String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

}
