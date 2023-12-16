package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.test.utils.NotificationUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    String scenario="";
    String action="";

    EditText edt_minute;
    EditText edt_heure;

    Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int PERMISSION_REQUEST_CODE = 1;
        Spinner spinner = findViewById(R.id.spinner);
        btn_send=findViewById(R.id.btn_send);
         edt_minute=findViewById(R.id.edt_minute);
         edt_heure=findViewById(R.id.edt_heure);

        NotificationUtils.showNotification(this, "Titre de la notification", "Contenu de la notification");


        edt_minute.setVisibility(View.GONE);
        edt_heure.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.names,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedName = parentView.getItemAtPosition(position).toString();
                scenario=selectedName;
                if(scenario.compareTo("Simple")==0){
                    edt_minute.setVisibility(View.GONE);
                    edt_heure.setVisibility(View.GONE);
                }
                else if(scenario.compareTo("Timer")==0){
                    edt_minute.setVisibility(View.VISIBLE);
                    edt_heure.setVisibility(View.GONE);
                } else if(scenario.compareTo("Scene")==0){
                    edt_minute.setVisibility(View.VISIBLE);
                    edt_heure.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    String choix = radioButton.getText().toString();
                    action=choix;
                }
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if(action!="" && scenario!=""){
                        try {
                            if(scenario.compareTo("Simple")==0){
                                sendSMS("+261344410934", action);
                            }
                            else if(scenario.compareTo("Timer")==0){
                               String minuteString= edt_minute.getText().toString().trim();
                               if(minuteString!="")
                               {
                                   sendSMS("+261344410934", action+"_"+minuteString);
                               }
                               else{
                                   Toast.makeText(MainActivity.this, "Ajouter la minute", Toast.LENGTH_SHORT).show();
                               }
                            } else if(scenario.compareTo("Scene")==0){
                                String minuteString= edt_minute.getText().toString().trim();
                                String heureString= edt_heure.getText().toString().trim();
                                if(minuteString!="" && heureString!="")
                                {
                                    sendSMS("+261344410934", action+"_"+heureString+"_"+minuteString);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Ajouter l'heure et minute", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Remplir le formulaire : ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestPermission();
                }
            }
        });
    }
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS envoyé avec succès.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'envoi du SMS.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btn_send.performClick();
            } else {
                Toast.makeText(this, "Permission refusée. Impossible d'envoyer le SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




