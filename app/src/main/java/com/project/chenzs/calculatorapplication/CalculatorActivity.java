package com.project.chenzs.calculatorapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
        final TextView currentText = findViewById(R.id.currentText);
        RelativeLayout buttonGroup = findViewById(R.id.numberGroup);

        for(int i=0;i<buttonGroup.getChildCount();i++){
            final Button btn = (Button)buttonGroup.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentText.append(btn.getText());
                }
            });
        }
    }
}
