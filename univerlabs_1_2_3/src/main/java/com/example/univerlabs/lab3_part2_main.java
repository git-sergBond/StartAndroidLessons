package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;
import com.example.univerlabs.store_db.v2.*;
import com.example.univerlabs.utils.database.View;
import com.example.univerlabs.utils.database.dateFormat;
import com.example.univerlabs.store_db.v2.lab3_classmatesContract.ClassmatesEntry;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class lab3_part2_main extends AppCompatActivity {

    lab3_classmatesDBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_part2_main);
        dbhelper = new lab3_classmatesDBHelper(this);
        Cursor cursor = dbhelper.getReadableDatabase().rawQuery("SELECT * from " + lab3_classmatesContract.ClassmatesEntry.TABLE_NAME,null);
        View.logCursor(cursor, "curr table");
    }


    private  void insertClassmater (String name, String surname, String patronymic) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassmatesEntry.COLLUMN_name, name);
        values.put(ClassmatesEntry.COLLUMN_surname, surname);
        values.put(ClassmatesEntry.COLLUMN_patronymic, patronymic);
        values.put(com.example.univerlabs.store_db.v1.lab3_classmatesContract.ClassmatesEntry.COLLUMN_date, dateFormat.getCurrentTime());
        long rowId = db.insert(com.example.univerlabs.store_db.v1.lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, null, values);
        if(rowId == -1) {
            Toast.makeText(this,"Error at insert row in sqlite", Toast.LENGTH_SHORT).show();
        } else  {
            Toast.makeText(this, "All ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void renameLastClassmater () {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(ClassmatesEntry.COLLUMN_name, "Иван");
        val.put(ClassmatesEntry.COLLUMN_surname, "Иванов");
        val.put(ClassmatesEntry.COLLUMN_patronymic, "Иванович");
        String id = null;
        Cursor cursor = db.query(ClassmatesEntry.TABLE_NAME, new String[] { ClassmatesEntry._ID }, null, null, null, null, ClassmatesEntry._ID + " DESC", "1");
        if(cursor != null) if(cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndex(ClassmatesEntry._ID));
        }
        if(id != null) {
            db.update(ClassmatesEntry.TABLE_NAME, val, ClassmatesEntry._ID + " = ?", new String[] {id} );
        }
    }

    public void addClassmate(android.view.View view) {
        insertClassmater("Alice1", "John2", "Bob3");
    }

    public void renameClassmater(android.view.View view) {
        renameLastClassmater();
    }

    public void showList(android.view.View view) {
        Intent i = new Intent(this, lab3_showClassmates_part2.class);
        startActivity(i);
    }
}
