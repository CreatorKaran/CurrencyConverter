package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.currencyconverter.InputFilter.DecimalInputFilter;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private Spinner spinner_firstChose, spinner_secondChose;
    HashMap<String,String> hashMap;
    private EditText edt_firstCountry, edt_secondCountry;
    private TextView txtview_result, TextView_date;
    String date;

    private TextToSpeech tts;
    private ImageButton tts_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hashMap = new HashMap<String, String>();
        initialize();
        getonlinemoneydata();
    }

    private void getonlinemoneydata() {
        
    }

    private void initialize() {
        spinner_firstChose =findViewById(R.id.spinner_firstChose);
        spinner_secondChose =findViewById(R.id.spinner_secondChose);
        edt_firstCountry =findViewById(R.id.edt_firstCountry);
        edt_secondCountry =findViewById(R.id.edt_secondCountry);
        txtview_result =findViewById(R.id.txtview_result);
        TextView_date =findViewById(R.id.TextView_date);
        tts_button =findViewById(R.id.tts_button);
        tts = new TextToSpeech(this, this);

        edt_secondCountry.setEnabled(false);
        edt_firstCountry.setFilters(new InputFilter[]{new DecimalInputFilter(5, 2)});

        spinner_secondChose.setSelection(0);
        spinner_firstChose.setSelection(1);
    }

    @Override
    public void onInit(int status) {

    }
}
