package com.example.bejeweled;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLite extends SQLiteOpenHelper {


    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BaseDatosBejeweled) {
        BaseDatosBejeweled.execSQL("create table jugadores(codigo text primary key, nombre text, puntaje int)");

    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
