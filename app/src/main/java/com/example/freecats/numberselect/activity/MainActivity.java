package com.example.freecats.numberselect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.freecats.numberselect.R;
import com.example.freecats.numberselect.view.RangeSliderWithNumber;
import com.example.freecats.numberselect.view.SeekBarWithNumber;

/**
 * Description: MainActivity of project
 * <br>Date: 2018-08-03 AM 10:06
 * <br>@author freecats
 */
public class MainActivity extends AppCompatActivity {

    TextView tvNumber;
    TextView tvBubble;
    TextView tvSbnBubble;
    RangeSliderWithNumber rsnNumber;
    RangeSliderWithNumber rsnBubble;
    SeekBarWithNumber sbnBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tvNumber = findViewById(R.id.tv_number);
        tvBubble = findViewById(R.id.tv_bubble);
        tvSbnBubble = findViewById(R.id.tv_sbn_bubble);
        rsnNumber = findViewById(R.id.rsn_number);
        rsnBubble = findViewById(R.id.rsn_bubble);
        sbnBubble = findViewById(R.id.sbn);


        rsnNumber.setRangeSliderListener(new RangeSliderWithNumber.RangeSliderListener() {
            @Override
            public void onMaxChanged(int newValue) {
                tvNumber.setText(rsnNumber.getSelectedMin() + "     " + rsnNumber.getSelectedMax());
            }

            @Override
            public void onMinChanged(int newValue) {
                tvNumber.setText(rsnNumber.getSelectedMin() + "     " + rsnNumber.getSelectedMax());
            }
        });


        rsnBubble.setStartingMinMax(60, 100);
        rsnBubble.setRangeSliderListener(new RangeSliderWithNumber.RangeSliderListener() {
            @Override
            public void onMaxChanged(int newValue) {
                tvBubble.setText(rsnBubble.getSelectedMin() + "     " + rsnBubble.getSelectedMax());
            }

            @Override
            public void onMinChanged(int newValue) {
                tvBubble.setText(rsnBubble.getSelectedMin() + "     " + rsnBubble.getSelectedMax());
            }
        });


        sbnBubble.setDefaultSelected(50);
        sbnBubble.setRangeSliderListener(new SeekBarWithNumber.NumberChangeListener() {
            @Override
            public void onNumberChange(int newValue) {
                tvSbnBubble.setText(String.valueOf(newValue));
            }
        });

    }
}
