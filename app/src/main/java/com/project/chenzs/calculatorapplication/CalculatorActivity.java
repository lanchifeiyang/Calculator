package com.project.chenzs.calculatorapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        initView();
    }

    private void initView() {
        TextView history = findViewById(R.id.history);
        TextView currentText = findViewById(R.id.currentText);
        RelativeLayout buttonGroup = findViewById(R.id.buttonGroup);

    }
}
