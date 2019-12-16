package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.currencyconverter.InputFilter.DecimalInputFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

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
        addTextWatcher();
        spinnerListener();
        buttonListener();

    }

    private void buttonListener() {
        tts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txtview_result.getText().toString().equals("")) {
                    tts.speak(txtview_result.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    RunAnimation(R.anim.clockwise,txtview_result);
                }

            }
        });
    }

    private void spinnerListener() {
        spinner_firstChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateAndSetResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_secondChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateAndSetResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addTextWatcher() {
        edt_firstCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndSetResult();
            }
        });
    }

    private void getonlinemoneydata() {
        hashMap.clear();
        hashMap.put("USD","1");
        hashMap.put("BDT", "85.45");

        String url ="http://data.fixer.io/latest?access_key=c9399a06e76d12753568973cb10c9bc2";

        JsonObjectRequest jsonObjReq= new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("tag", response.toString());

                try {
                    date = response.getString("date");
                    JSONObject phone = response.getJSONObject("rates");
                    String BGN = phone.getString("BGN");
                    String BRL = phone.getString("BRL");
                    String CAD = phone.getString("CAD");
                    String CHF = phone.getString("CHF");
                    String INR = phone.getString("INR");

                    hashMap.put("BGN", BGN);
                    hashMap.put("BRL", BRL);
                    hashMap.put("CAD", CAD);
                    hashMap.put("CHF", CHF);
                    hashMap.put("INR", INR);

                    TextView_date.setText("Last updated on " + date);
                    RunAnimation(R.anim.fade, TextView_date);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(jsonObjReq);


    }

    private void RunAnimation(int rID, TextView textView) {
        Animation a = AnimationUtils.loadAnimation(this,rID);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
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

    private void calculateAndSetResult() {
        if (edt_firstCountry.getText().toString().equals("")) {
            edt_firstCountry.setText("0");
        }

        if (!edt_firstCountry.getText().toString().equals("") || !edt_secondCountry.getText().toString().equals("")) {
            String inititCurrency = spinner_firstChose.getSelectedItem().toString();
            String targetCurrency = spinner_secondChose.getSelectedItem().toString();

            try {
            double baseRate = Double.valueOf(hashMap.get("USD"));
            double initRate = Double.valueOf(hashMap.get(inititCurrency));
            double targetRate = Double.valueOf(hashMap.get(targetCurrency));
            double first_input = Double.valueOf(edt_firstCountry.getText().toString());

            String resultFinal = String.valueOf(String.format("%.2f", ((targetRate * first_input) / initRate)));
            edt_secondCountry.setText(resultFinal);

            txtview_result.setText(edt_firstCountry.getText().toString() + " "
                    + inititCurrency + " = "+ resultFinal + " " + targetCurrency);
            RunAnimation(R.anim.blink,txtview_result);
            } catch (Exception e) {


            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.US);
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
