package com.example.univerlabs.store_db.v1;


import android.provider.BaseColumns;

public final class lab3_classmatesContract {

    private lab3_classmatesContract () {

    }

    public final class ClassmatesEntry implements BaseColumns {
        public final static String TABLE_NAME = "classmates";
        public final static String _ID = BaseColumns._ID;
        public final static String COLLUMN_FIO = "FIO";
        public final static String COLLUMN_date = "datestamp";
    }
}
