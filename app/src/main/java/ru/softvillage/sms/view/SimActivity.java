package ru.softvillage.sms.view;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.softvillage.sms.Common.SimEntity;
import ru.softvillage.sms.R;
import ru.softvillage.sms.modelMVP.DbHelper;
import ru.softvillage.sms.modelMVP.SimModel;
import ru.softvillage.sms.presenter.SimPresenter;

public class SimActivity extends AppCompatActivity {

    private SimPresenter presenter;
    LinearLayout simContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);

        init();
    }

    private void init() {
        simContentView = findViewById(R.id.sim_content);
        DbHelper dbHelper = new DbHelper(this);
        SimModel model = new SimModel(dbHelper);
        presenter = new SimPresenter(model);
        presenter.attachActivity(this);
    }

    public void showSims(List<SimEntity> sims) {
        for (SimEntity sim : sims) {
            TextView simView = simContentView.findViewWithTag(sim.getIccid());
            simView.setText(sim.toString());
        }
    }

}