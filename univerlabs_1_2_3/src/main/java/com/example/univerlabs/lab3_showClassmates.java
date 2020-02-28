package com.example.univerlabs;
import com.example.univerlabs.store_db.v1.lab3_classmatesContract;
import com.example.univerlabs.store_db.v1.lab3_classmatesDBHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class lab3_showClassmates extends AppCompatActivity {

    lab3_classmatesDBHelper dbhelper;

    class classmate {
        public String FIO = "";
        public String date = "";
        classmate(String FIO, String date) {
            this.FIO = FIO;
            this.date = date;
        }
    }

    public static class presentClassmate {

        public static void inflateView(classmate item, ViewGroup rootView, LayoutInflater inflater) {
            View itemView = inflater.inflate(R.layout.lab3_item_classmate_v2, rootView, false);

            ((TextView)itemView.findViewById(R.id.FIO)).setText(item.FIO);
            ((TextView)itemView.findViewById(R.id.datestamp)).setText(item.date);

            rootView.addView(itemView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_show_classmates);
        dbhelper = new lab3_classmatesDBHelper(this);
        LinearLayout classmatesListView = findViewById(R.id.classmatesList);
        ArrayList<classmate> classmatesListData = getListClassMates();

        LayoutInflater inflater = getLayoutInflater();

        for(classmate el : classmatesListData){
            presentClassmate.inflateView(el, classmatesListView, inflater);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ArrayList<classmate> getListClassMates() {

        ArrayList<classmate> result = new ArrayList<classmate>();

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String [] projection = {
                lab3_classmatesContract.ClassmatesEntry.COLLUMN_FIO,
                lab3_classmatesContract.ClassmatesEntry.COLLUMN_date
        };
        Cursor cursor = db.query(lab3_classmatesContract.ClassmatesEntry.TABLE_NAME, projection, null, null, null, null, null);
        try {
            int FIO_index = cursor.getColumnIndex(lab3_classmatesContract.ClassmatesEntry.COLLUMN_FIO);
            int date_index = cursor.getColumnIndex(lab3_classmatesContract.ClassmatesEntry.COLLUMN_date);

            while (cursor.moveToNext()) {
                String FIO = cursor.getString(FIO_index);
                String date = cursor.getString(date_index);
                result.add(new classmate(FIO, date));
            }

        } finally {
            cursor.close();
        }

        return result;
    }
}
