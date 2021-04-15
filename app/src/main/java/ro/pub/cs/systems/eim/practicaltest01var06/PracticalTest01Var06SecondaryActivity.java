package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static ro.pub.cs.systems.eim.practicaltest01var06.Constants.*;

public class PracticalTest01Var06SecondaryActivity extends AppCompatActivity {
    private EditText gainedText;
    private Button okButton;
    private int gained;

    private final ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ok_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06MainActivity.class);
                    intent.putExtra(SCOR, gained);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_secondary);

        gainedText = (EditText) findViewById(R.id.gained_text);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(buttonClickListener);

        Intent intentFromParent = getIntent();

        if (intentFromParent != null) {
            int checks = intentFromParent.getIntExtra(NR_CHECKS, 0);
            String firstNr = intentFromParent.getStringExtra(FIRST_NUM);
            String secondNr = intentFromParent.getStringExtra(SECOND_NUM);
            String thirdNr = intentFromParent.getStringExtra(THIRD_NUM);
            if (checkGained(firstNr, secondNr, thirdNr)) {
                if (checks == 0) {
                    gained = 100;
                } else if (checks == 1) {
                    gained = 50;
                } else
                    gained = 10;
                gainedText.setText(String.valueOf(gained));
            }
        }

    }

    private boolean checkGained(String firstNr, String secondNr, String thirdNr) {
        if (firstNr.equals(secondNr) && secondNr.equals(thirdNr))
            return true;
        return false;
    }

}
