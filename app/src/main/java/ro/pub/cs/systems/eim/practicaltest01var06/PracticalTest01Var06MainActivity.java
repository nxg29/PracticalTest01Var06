package ro.pub.cs.systems.eim.practicaltest01var06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static ro.pub.cs.systems.eim.practicaltest01var06.Constants.*;

public class PracticalTest01Var06MainActivity extends AppCompatActivity {
    private Button playButton;
    private EditText firstNr, secondNr, thirdNr;
    private CheckBox firstCheckBox, secondCheckBox, thirdCheckBox;
    private int scor = 0;


    private int serviceStatus = SERVICE_STOPPED;

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    private IntentFilter intentFilter = new IntentFilter();

    private final ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.play_button:
                    int checks = 3;
                    if (!firstCheckBox.isChecked()) {
                        checks--;
                        firstNr.setText(generateRandom());
                    }
                    if (!secondCheckBox.isChecked()) {
                        checks--;
                        secondNr.setText(generateRandom());
                    }
                    if (!thirdCheckBox.isChecked()) {
                        checks--;
                        thirdNr.setText(generateRandom());
                    }
                    String nums = firstNr.getText().toString()
                            + " " + secondNr.getText().toString() + " " + thirdNr.getText().toString();
                    Toast.makeText(getApplicationContext(),"NUMBERS: " + nums,
                            Toast.LENGTH_LONG).show();

                    if (scor > THRESHOLD || scor > 0) {
                        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
                        intent.putExtra(SCOR, scor);
                        getApplicationContext().startService(intent);
                        serviceStatus = Constants.SERVICE_STARTED;
                    }

                    if (scor  == getScore(firstNr.getText().toString(), secondNr.getText().toString(), thirdNr.getText().toString(), checks)) {
                        Toast.makeText(getApplicationContext(), "SCOR: " + scor, Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06SecondaryActivity.class);
                        intent.putExtra(NR_CHECKS, checks);
                        intent.putExtra(FIRST_NUM, firstNr.getText().toString());
                        intent.putExtra(SECOND_NUM, secondNr.getText().toString());
                        intent.putExtra(THIRD_NUM, thirdNr.getText().toString());
                        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                    }
                    break;
            }
        }
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG", intent.getStringExtra(BROADCAST_RECEIVER_EXTRA));
            Toast.makeText(getApplicationContext(), intent.getStringExtra(BROADCAST_RECEIVER_EXTRA) , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_main);

        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(buttonClickListener);

        firstNr = (EditText) findViewById(R.id.first_nr);
        secondNr = (EditText) findViewById(R.id.second_nr);
        thirdNr = (EditText) findViewById(R.id.third_nr);

        firstCheckBox = (CheckBox) findViewById(R.id.first_checkbox);
        secondCheckBox = (CheckBox) findViewById(R.id.second_checkbox);
        thirdCheckBox = (CheckBox) findViewById(R.id.third_checkbox);

        if (savedInstanceState !=  null) {
            scor = savedInstanceState.getInt(SCOR, 0);
        }

        intentFilter.addAction(Constants.INTENT_FILTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
        stopService(intent);
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(SCOR, scor);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        scor = savedInstanceState.getInt(SCOR, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            scor += intent.getIntExtra(SCOR, 0);
            Toast.makeText(this, "SCOR: " + scor, Toast.LENGTH_LONG).show();
        }
    }

    private int getScore(String firstNr, String secondNr, String thirdNr, int checks) {
        int gained = 0;
        if (checkGained(firstNr, secondNr, thirdNr)) {
            if (checks == 0) {
                gained = 100;
            } else if (checks == 1) {
                gained = 50;
            } else
                gained = 10;
        }
        return gained;
    }

    private boolean checkGained(String firstNr, String secondNr, String thirdNr) {
        if (firstNr.equals(secondNr) && secondNr.equals(thirdNr))
            return true;
        return false;
    }

    private String generateRandom() {
        String [] arr = {"1", "2", "3", "*"};
        Random random = new Random();
        int select = random.nextInt(arr.length);
        return arr[select];
    }
}