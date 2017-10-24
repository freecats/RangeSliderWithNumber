package com.example.freecats.numberselect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.freecats.numberselect.R;
import com.example.freecats.numberselect.view.RangeSliderWithNumber;
import com.example.freecats.numberselect.view.SeekBarWithNumber;

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
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvBubble = (TextView) findViewById(R.id.tv_bubble);
        tvSbnBubble = (TextView) findViewById(R.id.tv_sbn_bubble);
        rsnNumber = (RangeSliderWithNumber) findViewById(R.id.rsn_number);
        rsnBubble = (RangeSliderWithNumber) findViewById(R.id.rsn_bubble);
        sbnBubble = (SeekBarWithNumber) findViewById(R.id.sbn);


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
