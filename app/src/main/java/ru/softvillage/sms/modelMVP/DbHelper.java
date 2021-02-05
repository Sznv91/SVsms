package ru.softvillage.sms.modelMVP;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ru.softvillage.sms.Common.SimTable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String NAME = "sms";
    public static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SimTable.CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
