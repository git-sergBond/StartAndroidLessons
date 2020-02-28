package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;
import com.example.univerlabs.store_db.v1.lab3_classmatesDBHelper;
import com.example.univerlabs.store_db.v1.lab3_classmatesContract;
import com.example.univerlabs.utils.database.dateFormat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class lab3_main extends AppCompatActivity {

    private lab3_classmatesDBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_main);
        /**
         * CREATE TABLE classmates (id integer primary key, FIO varchar(200), datestamp date )
         */
        /*
INSERT INTO classmates
            (FIO, datestamp)
     VALUES ("Сергей Сергеевич Бондаренко", strftime('%H:%M / %Y-%m-%d';, 'now'));*/
        /* SELECT * from classmates */
        dbhelper = new lab3_classmatesDBHelper(this);
        deleteClassmaters();
        insertClassmater("Бондаренко1 Сергей Сергеевич");
        insertClassmater("Бондаренко2 Сергей Сергеевич");
        insertClassmater("Бондаренко3 Сергей Сергеевич");
        insertClassmater("Бондаренко4 Сергей Сергеевич");
        insertClassmater("Бондаренко5 Сергей Сергеевич");
        Log.d("sql", "all ok");
        Cursor cursor = dbhelper.getReadableDatabase().rawQuery("SELECT * from " + com.example.univerlabs.store_db.v2.lab3_classmatesContract.ClassmatesEntry.TABLE_NAME,null);
        com.example.univerlabs.utils.database.View.logCursor(cursor, "123");
    }
/*
    boolean isRestart = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
        Log.d("start1", "restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isRestart) {
            Log.d("start1", "start");
            deleteClassmaters();
            insertClassmater("Бондаренко1 Сергей Сергеевич");
            insertClassmater("Бондаренко2 Сергей Сергеевич");
            insertClassmater("Бондаренко3 Сергей Сергеевич");
            insertClassmater("Бондаренко4 Сергей Сергеевич");
            insertClassmater("Бондаренко5 Сергей Сергеевич");
        }
    }*/

    public void goToClassmatesActivity(View view) {
        Intent i = new Intent(this, lab3_showClassmates.class);
        startActivity(i);
    }

    private  void deleteClassmaters () {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, null, null);
    }

    private  void insertClassmater (String FIO) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(lab3_classmatesContract.ClassmatesEntry.COLLUMN_FIO, FIO);
        values.put(lab3_classmatesContract.ClassmatesEntry.COLLUMN_date, dateFormat.getCurrentTime());
        long rowId = db.insert(lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, null, values);
        if(rowId == -1) {
            Toast.makeText(this,"Error at insert row in sqlite", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(this, "All ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void renameLastClassmater () {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(lab3_classmatesContract.ClassmatesEntry.COLLUMN_FIO, "Иванов Иван Иванович");
        String id = null;
        Cursor cursor = db.query(lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, new String[] { lab3_classmatesContract.ClassmatesEntry._ID }, null, null, null, null, lab3_classmatesContract.ClassmatesEntry._ID + " DESC", "1");
        if(cursor != null) if(cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(lab3_classmatesContract.ClassmatesEntry._ID));
        }
        if(id != null) {
            db.update(lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, val, lab3_classmatesContract.ClassmatesEntry._ID + " = ?", new String[] {id} );
        }
    }

    public void addClassmate(View view) {
        insertClassmater("Alice John Bob");
    }

    public void renameClassmater(View view) {
        renameLastClassmater();
    }
}
