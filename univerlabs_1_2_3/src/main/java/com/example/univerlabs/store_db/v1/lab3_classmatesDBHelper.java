package com.example.univerlabs.store_db.v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class lab3_classmatesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = lab3_classmatesDBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "classmates.db";

    public static final int DATABASE_VERSION = 1;

    public lab3_classmatesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Create_classmates = String.format(
                "CREATE TABLE %s " +
                             "(%s integer primary key, " +
                             "%s varchar(200), " +
                             "%s date )",
                lab3_classmatesContract.ClassmatesEntry.TABLE_NAME,
                lab3_classmatesContract.ClassmatesEntry._ID,
                lab3_classmatesContract.ClassmatesEntry.COLLUMN_FIO,
                lab3_classmatesContract.ClassmatesEntry.COLLUMN_date);
        sqLiteDatabase.execSQL(Create_classmates);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /*
        // Запишем в журнал
	Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

	// Удаляем старую таблицу и создаём новую
	db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
	// Создаём новую таблицу
	onCreate(db);
         */
    }
}
