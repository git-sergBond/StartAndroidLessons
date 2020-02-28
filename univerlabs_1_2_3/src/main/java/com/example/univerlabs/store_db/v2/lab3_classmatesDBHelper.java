package com.example.univerlabs.store_db.v2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.univerlabs.store_db.v2.lab3_classmatesContract.ClassmatesEntry;

public class lab3_classmatesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = lab3_classmatesDBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "classmates.db";

    public static final int DATABASE_VERSION = 2;

    public lab3_classmatesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String Create_tb_classmates = "CREATE TABLE %s " +
            "(%s integer primary key, " +
            "%s varchar(200), " +
            "%s varchar(200), " +
            "%s varchar(200), " +
            "%s date );";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Create_classmates = String.format(
                Create_tb_classmates,
                ClassmatesEntry.TABLE_NAME,
                ClassmatesEntry._ID,
                ClassmatesEntry.COLLUMN_name,
                ClassmatesEntry.COLLUMN_surname,
                ClassmatesEntry.COLLUMN_patronymic,
                ClassmatesEntry.COLLUMN_date);
        sqLiteDatabase.execSQL(Create_classmates);
    }

    public void upgrade (SQLiteDatabase db) {
        String tmp_table = "tmp_table";

        String drop_tmp_table = String.format("DROP TABLE IF EXISTS %s;", tmp_table);

        String add_tmp_table = String.format(Create_tb_classmates,
                tmp_table,
                ClassmatesEntry._ID,
                ClassmatesEntry.COLLUMN_name,
                ClassmatesEntry.COLLUMN_surname,
                ClassmatesEntry.COLLUMN_patronymic,
                ClassmatesEntry.COLLUMN_date);

        String split_FIO =
                "SELECT "+ClassmatesEntry._ID+","+ lab3_classmatesContract.ClassmatesEntry.COLLUMN_name+"," +
                        "       substr(surname_patronimyc, 1, instr(surname_patronimyc, ' ')-1) as "+ lab3_classmatesContract.ClassmatesEntry.COLLUMN_surname+ "," +
                        "       substr(surname_patronimyc, instr(surname_patronimyc, ' ') +1) as "+ lab3_classmatesContract.ClassmatesEntry.COLLUMN_patronymic + "," +
                                ClassmatesEntry.COLLUMN_date +
                        "       FROM (" +
                        "            SELECT "+ClassmatesEntry._ID+","+ lab3_classmatesContract.ClassmatesEntry.DEPRECATED_COLLUMN_FIO+"," +
                        "                   substr("+ lab3_classmatesContract.ClassmatesEntry.DEPRECATED_COLLUMN_FIO+", 1, instr("+lab3_classmatesContract.ClassmatesEntry.DEPRECATED_COLLUMN_FIO+", ' ')-1) as "+ lab3_classmatesContract.ClassmatesEntry.COLLUMN_name+"," +
                        "                   substr("+ lab3_classmatesContract.ClassmatesEntry.DEPRECATED_COLLUMN_FIO+", instr("+lab3_classmatesContract.ClassmatesEntry.DEPRECATED_COLLUMN_FIO+", ' ') +1) as surname_patronimyc, " +
                                             ClassmatesEntry.COLLUMN_date +
                        "              FROM "+ lab3_classmatesContract.ClassmatesEntry.TABLE_NAME+");";

        String fill_tmp_table = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) %s;",
                tmp_table,
                ClassmatesEntry._ID,
                ClassmatesEntry.COLLUMN_name,
                ClassmatesEntry.COLLUMN_surname,
                ClassmatesEntry.COLLUMN_patronymic,
                ClassmatesEntry.COLLUMN_date,
                split_FIO);

        String drop_old_table = String.format("DROP TABLE IF EXISTS %s;", ClassmatesEntry.TABLE_NAME);

        String rename_table = String.format("ALTER TABLE %s RENAME TO %s;", tmp_table, ClassmatesEntry.TABLE_NAME);

        db.execSQL("BEGIN TRANSACTION;");
        db.execSQL(drop_tmp_table);
        db.execSQL(add_tmp_table);
        db.execSQL(fill_tmp_table);
        db.execSQL(drop_old_table);
        db.execSQL(rename_table);
        db.execSQL("COMMIT;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("upgrade-database", "update sqlite from v" + String.valueOf(i) + " on v" + String.valueOf(i1));
        upgrade(sqLiteDatabase);
    }
}
