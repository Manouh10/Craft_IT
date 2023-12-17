package com.example.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.utils.NotificationUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final int PERMISSION_REQUEST_CODE = 1;
    String scenario="";
    String action="";

    EditText edt_minute;
    EditText edt_heure;
    EditText edt_seconde;
    View rootView;

    ImageView btn_send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        final int PERMISSION_REQUEST_CODE = 1;
        Spinner spinner = rootView.findViewById(R.id.spinner);
        btn_send= rootView.findViewById(R.id.btn_send);
        edt_minute= rootView.findViewById(R.id.edt_minute);
        edt_heure= rootView.findViewById(R.id.edt_heure);
        edt_seconde= rootView.findViewById(R.id.edt_seconde);



        edt_minute.setVisibility(View.GONE);
        edt_heure.setVisibility(View.GONE);
        edt_seconde.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
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
                    edt_seconde.setVisibility(View.GONE);
                }
                else if(scenario.compareTo("Scene")==0){
                    edt_minute.setVisibility(View.VISIBLE);
                    edt_heure.setVisibility(View.VISIBLE);
                    edt_seconde.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        RadioGroup radioGroup = rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = rootView.findViewById(checkedId);
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
                                sendSMS( action);
                            }
                          else if(scenario.compareTo("Scene")==0){
                                String minuteString= edt_minute.getText().toString().trim();
                                String heureString= edt_heure.getText().toString().trim();
                                String secondeString= edt_seconde.getText().toString().trim();
                                if(minuteString!="" && heureString!="" && secondeString!="")
                                {
                                    sendSMS( action+"_"+heureString+"_"+minuteString+"_"+secondeString);
                                }
                                else{
                                    Toast.makeText(getContext(), "Ajouter l'heure , minute et seconde", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Remplir le formulaire : ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestPermission();
                }
            }
        });

        return rootView;
    }
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
    }

    private void sendSMS( String message) {
        try {
            String phoneNumber="+261348672345";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getContext(), "SMS envoyé avec succès.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erreur lors de l'envoi du SMS.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Permission refusée. Impossible d'envoyer le SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

