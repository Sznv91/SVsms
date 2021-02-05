package ru.softvillage.sms.modelMVP;

import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ru.softvillage.sms.Common.SimEntity;
import ru.softvillage.sms.Common.SimTable;

public class SimModel {
    DbHelper dbHelper;

    public SimModel(DbHelper helper) {
        this.dbHelper = helper;
    }

    public void loadSims(LoadSimCallback callback) {
        LoadSimTask loadSimTask = new LoadSimTask(callback);
        loadSimTask.execute();
    }

    public interface LoadSimCallback {
        void onLoad(List<SimEntity> sims);
    }

    class LoadSimTask extends AsyncTask<Void, Void, List<SimEntity>> {

        private final LoadSimCallback callback;

        LoadSimTask(LoadSimCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<SimEntity> doInBackground(Void... voids) {
            List<SimEntity> sims = new ArrayList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(SimTable.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()){
                SimEntity sim = new SimEntity();
                sim.setIccid(cursor.getString(cursor.getColumnIndex(SimTable.COLUMN.ID)));
                sim.setOperatorName(cursor.getString(cursor.getColumnIndex(SimTable.COLUMN.OPERATOR)));
                sim.setSlotNumber(cursor.getInt(cursor.getColumnIndex(SimTable.COLUMN.SLOT)));
                sim.setSecureCode(cursor.getInt(cursor.getColumnIndex(SimTable.COLUMN.SECURE_CODE)));
                sim.setPhoneNumber(cursor.getString(cursor.getColumnIndex(SimTable.COLUMN.PHONE_NUMBER)));
                sims.add(sim);
            }
            cursor.close();
            return sims;
        }

        @Override
        protected void onPostExecute(List<SimEntity> simEntities) {
            if (callback != null){
                callback.onLoad(simEntities);
            }
        }
    }

}
