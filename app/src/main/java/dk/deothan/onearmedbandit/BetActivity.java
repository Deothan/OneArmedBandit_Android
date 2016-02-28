package dk.deothan.onearmedbandit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

/**
 * Created by Mads on 28-02-2016.
 */
public class BetActivity extends Activity {
    Spinner spinner;
    BanditData banditData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        banditData = (BanditData)getApplication();

        spinner = (Spinner) findViewById(R.id.spinner);
    }

    public void betBtnClicked(View view) {
        banditData.setBet(Integer.parseInt(spinner.getSelectedItem().toString()));
        finish();
    }
}
