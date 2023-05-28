package com.rehan.mytaxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private TextView rrspVal;
    private Slider rrspSlider;
    private EditText incomeVal;
    private TextView calculatedValue;
    private TextView rrspRemaining;

    public String inc;
    public String sldr;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";
    public static final String SLIDER = "slider";

    private double income = 0.0;
    private double rrsp = 0.0;
    private double taxableAmt = 0.0;
    private double totalTax = 0.0;

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(INCOME, Double.toString(income));
        editor.putString(SLIDER, Double.toString(rrsp));

        editor.apply();

    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        inc = sharedPreferences.getString(INCOME, "0.0");
        sldr = sharedPreferences.getString(SLIDER, "0.0");
    }

    public void updateViews() {
        rrspVal.setText(sldr);
        incomeVal.setText(inc);
    }

    public void incomeChanged() {
        totalTax = 0.0;
        if(incomeVal.getText().length() > 0) {
            income = Double.parseDouble(String.valueOf(incomeVal.getText()))  ;
        }
        System.out.println(income);
        taxableAmt = income - rrsp;
        System.out.println("Taxabel amout" + taxableAmt);
        if(taxableAmt <= 0 ) {
            taxableAmt = 0.0;
            totalTax = 0.0;
        } else {
            while (taxableAmt > 0) {
                if(taxableAmt > 220000 ) {
                    totalTax += (taxableAmt - 220000) *  0.136;
                    taxableAmt = 220000;
                }  if(taxableAmt > 150000) {
                    totalTax += (taxableAmt - 150000) * 0.1216;
                    taxableAmt = 150000;
                }  if(taxableAmt > 98463) {
                    totalTax += (taxableAmt - 98463) * 0.1116;
                    taxableAmt = 98463;
                }  if(taxableAmt > 49231) {
                    totalTax += (taxableAmt - 49231) * 0.0915;
                    taxableAmt = 49231;
                } else {
                    totalTax += taxableAmt * 0.0505;
                    taxableAmt = 0;
                }
            }
        }
        System.out.println(totalTax);
        calculatedValue.setText(df.format(totalTax));
        saveData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

        rrspVal = findViewById(R.id.RRSPVal);
        rrspSlider = findViewById(R.id.RRSPSlider);
        incomeVal = findViewById(R.id.incomeVal);
        calculatedValue = findViewById(R.id.calculatedVal);
        rrspRemaining = findViewById(R.id.calculatedRRSP);
        updateViews();
        incomeVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                incomeChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rrspSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                rrspVal.setText(Float.toString(value));
                float rr = 27830 - value ;
                rrspRemaining.setText(Float.toString(rr));
                rrsp = Double.parseDouble(Float.toString(value));
                System.out.println(rrsp);
                incomeChanged();
            }
        });


    }

}
