package com.example.univerlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.univerlabs.store_db.v2.lab3_classmatesDBHelper;
import com.example.univerlabs.store_db.v2.lab3_classmatesContract.ClassmatesEntry;

import java.util.ArrayList;

public class lab3_showClassmates_part2 extends AppCompatActivity {

    ListView list = null;
    lab3_classmatesDBHelper dbhelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_show_classmates_part2);

        list = findViewById(R.id.list);
        dbhelper = new lab3_classmatesDBHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lab3_part2_item_classmate_v1, getListClassMates());

        list.setAdapter(adapter);
    }


    private ArrayList<String> getListClassMates() {

        ArrayList<String> result = new ArrayList<String>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] projection = {
                ClassmatesEntry.COLLUMN_name,
                ClassmatesEntry.COLLUMN_surname,
                ClassmatesEntry.COLLUMN_patronymic,
                ClassmatesEntry.COLLUMN_date
        };
        Cursor cursor = db.query(ClassmatesEntry.TABLE_NAME, projection, null, null, null, null, null);
        try {
            int COLLUMN_name = cursor.getColumnIndex(ClassmatesEntry.COLLUMN_name);
            int COLLUMN_surname = cursor.getColumnIndex(ClassmatesEntry.COLLUMN_surname);
            int COLLUMN_patronymic = cursor.getColumnIndex(ClassmatesEntry.COLLUMN_patronymic);
            int date_index = cursor.getColumnIndex(ClassmatesEntry.COLLUMN_date);

            while (cursor.moveToNext()) {
                String str = cursor.getString(COLLUMN_name) + " " + cursor.getString(COLLUMN_surname) + " " + cursor.getString(COLLUMN_patronymic) + " " + cursor.getString(date_index);
                result.add(str);
            }

        } finally {
            cursor.close();
        }

        return result;
    }
}
