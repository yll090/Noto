package com.accedia.noto.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.accedia.noto.model.Word;

@android.arch.persistence.room.Database(entities = {Word.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public static final String DATABASE_NAME = "word_database";

    public static Database getDatabase(final Context context, boolean inMemory) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    if (inMemory) {
                        INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                                Database.class)
                                .build();
                    } else {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                Database.class, DATABASE_NAME)
                                .addCallback(creationCallback)
                                .build();
                    }
                }
            }
        }
        return INSTANCE;
    }

    public abstract WordDAO wordDAO();

    private static RoomDatabase.Callback creationCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDAO wordDAO;

        PopulateDbAsync(Database db) {
            wordDAO = db.wordDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            wordDAO.deleteAll();
            Word word = new Word("Hello");
            wordDAO.insert(word);
            word = new Word("World");
            wordDAO.insert(word);
            return null;
        }
    }
}
