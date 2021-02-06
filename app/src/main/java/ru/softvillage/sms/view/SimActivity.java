package ru.softvillage.sms.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.softvillage.sms.R;
import ru.softvillage.sms.model.Entity.Sim;
import ru.softvillage.sms.presenter.SimPresenter;

public class SimActivity extends AppCompatActivity {
    SimPresenter presenter;
    LinearLayout mainLayout;
    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);
        init();
    }

    private void init() {
        findViewById(R.id.exit_button).setOnClickListener(v -> System.exit(1));
        mainLayout = findViewById(R.id.display_sim);
        status = findViewById(R.id.status);
        if (presenter == null){
            presenter = new SimPresenter(SimActivity.this);
        }
    }

    public void showSims(List<Sim> simList) {
        for (Sim sim : simList) {
            TextView singleSim = mainLayout.findViewWithTag(sim.getIccid());
            singleSim.setText(sim.toString());
        }
    }

    public void initSims(List<Sim> simList) {
        for (Sim sim : simList) {
            TextView singleSim = new TextView(this);
            singleSim.setTag(sim.getIccid());
            singleSim.setText(sim.toString());
            singleSim.setPadding(20, 20, 20, 20);
            mainLayout.addView(singleSim);
        }
    }

    public void showStatus(String status){
        this.status.setText(status);
    }
}