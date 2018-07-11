package com.ceu.lavanderia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;

import com.ceu.lavanderia.model.Agendamento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.WriteBatch;

public class AgendamentoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "AgendamentoActivity";
    private static final String AVATAR_IMAGE_URL = "https://www.w3schools.com/howto/img_avatar.png";

    GridLayout timeGroup;
    Button confirmButton;
    private FirebaseFirestore mFirestore;

    TextView t8, t9, t10, t11, t12, t13, t14, t15, t16, t17;
    Button b8, b9, b10, b11, b12, b13, b14, b15, b16, b17;
    int h8, h9, h10, h11, h12, h13, h14, h15, h16, h17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        mFirestore = FirebaseFirestore.getInstance();

        timeGroup = findViewById(R.id.time_group);
        t8 = findViewById(R.id.t8);
        t9 = findViewById(R.id.t9);
        t10 = findViewById(R.id.t10);
        t11 = findViewById(R.id.t11);
        t12 = findViewById(R.id.t12);
        t13 = findViewById(R.id.t13);
        t14 = findViewById(R.id.t14);
        t15 = findViewById(R.id.t15);
        t16 = findViewById(R.id.t16);
        t17 = findViewById(R.id.t17);

        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b10 = findViewById(R.id.b10);
        b11 = findViewById(R.id.b11);
        b12 = findViewById(R.id.b12);
        b13 = findViewById(R.id.b13);
        b14 = findViewById(R.id.b14);
        b15 = findViewById(R.id.b15);
        b16 = findViewById(R.id.b16);
        b17 = findViewById(R.id.b17);

        timeGroup.setVisibility(View.GONE);
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setVisibility(View.GONE);

        final EditText openCalendarField = findViewById(R.id.open_calendar_field);
        openCalendarField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                b8.getBackground().clearColorFilter();
                b9.getBackground().clearColorFilter();
                b10.getBackground().clearColorFilter();
                b11.getBackground().clearColorFilter();
                b12.getBackground().clearColorFilter();
                b13.getBackground().clearColorFilter();
                b14.getBackground().clearColorFilter();
                b15.getBackground().clearColorFilter();
                b16.getBackground().clearColorFilter();
                b17.getBackground().clearColorFilter();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

        final EditText openCalendarField = findViewById(R.id.open_calendar_field);
        openCalendarField.setText(currentDateString);

        String doc = currentDateString.replaceAll("/", "-");
        StringBuilder str = new StringBuilder(doc);
        str.insert(6, '2');
        str.insert(7, '0');

        DocumentReference docRef = mFirestore.collection("agendamentos_controle")
                .document(str.toString());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> documentData = document.getData();

                        h8 = 5 - ((Long) documentData.get("h8")).intValue();
                        h9 = 5 - ((Long) documentData.get("h9")).intValue();
                        h10= 5 - ((Long) documentData.get("h10")).intValue();
                        h11 = 5 - ((Long) documentData.get("h11")).intValue();
                        h12 = 5 - ((Long) documentData.get("h12")).intValue();
                        h13 = 5 - ((Long) documentData.get("h13")).intValue();
                        h14 = 5 - ((Long) documentData.get("h14")).intValue();
                        h15 = 5 - ((Long) documentData.get("h15")).intValue();
                        h16 = 5 - ((Long) documentData.get("h16")).intValue();
                        h17 = 5 - ((Long) documentData.get("h17")).intValue();

                        t8.setText(getResources().getQuantityString(R.plurals.availableMachines, h8, h8));
                        t9.setText(getResources().getQuantityString(R.plurals.availableMachines, h9, h9));
                        t10.setText(getResources().getQuantityString(R.plurals.availableMachines, h10, h10));
                        t11.setText(getResources().getQuantityString(R.plurals.availableMachines, h11, h11));
                        t12.setText(getResources().getQuantityString(R.plurals.availableMachines, h12, h12));
                        t13.setText(getResources().getQuantityString(R.plurals.availableMachines, h13, h13));
                        t14.setText(getResources().getQuantityString(R.plurals.availableMachines, h14, h14));
                        t15.setText(getResources().getQuantityString(R.plurals.availableMachines, h15, h15));
                        t16.setText(getResources().getQuantityString(R.plurals.availableMachines, h16, h16));
                        t17.setText(getResources().getQuantityString(R.plurals.availableMachines, h17, h17));

                        if (h8 == 0) {
                            t8.setEnabled(false);
                            b8.setEnabled(false);
                        } else {
                            t8.setEnabled(true);
                            b8.setEnabled(true);
                        }
                        if (h9 == 0) {
                            t9.setEnabled(false);
                            b9.setEnabled(false);
                        } else {
                            t9.setEnabled(true);
                            b9.setEnabled(true);
                        }
                        if (h10 == 0){
                            t10.setEnabled(false);
                            b10.setEnabled(false);
                        } else {
                            t10.setEnabled(true);
                            b10.setEnabled(true);
                        }
                        if (h11 == 0){
                            t11.setEnabled(false);
                            b11.setEnabled(false);
                        } else {
                            t11.setEnabled(true);
                            b11.setEnabled(true);
                        }
                        if (h12 == 0){
                            t12.setEnabled(false);
                            b12.setEnabled(false);
                        } else {
                            t12.setEnabled(true);
                            b12.setEnabled(true);
                        }
                        if (h13 == 0){
                            t13.setEnabled(false);
                            b13.setEnabled(false);
                        } else {
                            t13.setEnabled(true);
                            b13.setEnabled(true);
                        }
                        if (h14 == 0){
                            t14.setEnabled(false);
                            b14.setEnabled(false);
                        } else {
                            t14.setEnabled(true);
                            b14.setEnabled(true);
                        }
                        if (h15 == 0){
                            t15.setEnabled(false);
                            b15.setEnabled(false);
                        } else {
                            t15.setEnabled(true);
                            b15.setEnabled(true);
                        }
                        if (h16 == 0){
                            t16.setEnabled(false);
                            b16.setEnabled(false);
                        } else {
                            t16.setEnabled(true);
                            b16.setEnabled(true);
                        }
                        if (h17 == 0){
                            t17.setEnabled(false);
                            b17.setEnabled(false);
                        } else {
                            t17.setEnabled(true);
                            b17.setEnabled(true);
                        }

                        setOnClick(b8, h8, t8, openCalendarField);
                        setOnClick(b9, h9, t9, openCalendarField);
                        setOnClick(b10, h10, t10, openCalendarField);
                        setOnClick(b11, h11, t11, openCalendarField);
                        setOnClick(b12, h12, t12, openCalendarField);
                        setOnClick(b13, h13, t13, openCalendarField);
                        setOnClick(b14, h14, t14, openCalendarField);
                        setOnClick(b15, h15, t15, openCalendarField);
                        setOnClick(b16, h16, t16, openCalendarField);
                        setOnClick(b17, h17, t17, openCalendarField);

                    } else {
                        Log.d(TAG, "No such document");

                        t8.setText("5 máquinas\ndisponíveis");
                        t9.setText("5 máquinas\ndisponíveis");
                        t10.setText("5 máquinas\ndisponíveis");
                        t11.setText("5 máquinas\ndisponíveis");
                        t12.setText("5 máquinas\ndisponíveis");
                        t13.setText("5 máquinas\ndisponíveis");
                        t14.setText("5 máquinas\ndisponíveis");
                        t15.setText("5 máquinas\ndisponíveis");
                        t16.setText("5 máquinas\ndisponíveis");
                        t17.setText("5 máquinas\ndisponíveis");

                        t8.setEnabled(true);
                        b8.setEnabled(true);
                        t9.setEnabled(true);
                        b9.setEnabled(true);
                        t10.setEnabled(true);
                        b10.setEnabled(true);
                        t11.setEnabled(true);
                        b11.setEnabled(true);
                        t12.setEnabled(true);
                        b12.setEnabled(true);
                        t13.setEnabled(true);
                        b13.setEnabled(true);
                        t14.setEnabled(true);
                        b14.setEnabled(true);
                        t15.setEnabled(true);
                        b15.setEnabled(true);
                        t16.setEnabled(true);
                        b16.setEnabled(true);
                        t17.setEnabled(true);
                        b17.setEnabled(true);

                        setOnClick(b8, 5, t8, openCalendarField);
                        setOnClick(b9, 5, t9, openCalendarField);
                        setOnClick(b10, 5, t10, openCalendarField);
                        setOnClick(b11, 5, t11, openCalendarField);
                        setOnClick(b12, 5, t12, openCalendarField);
                        setOnClick(b13, 5, t13, openCalendarField);
                        setOnClick(b14, 5, t14, openCalendarField);
                        setOnClick(b15, 5, t15, openCalendarField);
                        setOnClick(b16, 5, t16, openCalendarField);
                        setOnClick(b17, 5, t17, openCalendarField);
                    }

                    confirmarAgendamento(openCalendarField);

                    timeGroup.setVisibility(View.VISIBLE);
                    confirmButton.setVisibility(View.VISIBLE);

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void setOnClick(final Button btn, final int h, final TextView t, final EditText openCalendarField){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = t.getText().toString();

                if (text == getResources().getString(R.string.selecao)) {
                    t.setText(getResources().getQuantityString(R.plurals.availableMachines, h, h));
                    btn.getBackground().clearColorFilter();
                } else {
                    t.setText(R.string.selecao);
                    btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                }

            }
        });
    }

    private void confirmarAgendamento(final EditText openCalendarField){
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                Uri photoUrl;
                String uid;

                Agendamento agendamento = new Agendamento();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // Name, email address, and profile photo Url
                    name = user.getDisplayName();
                    photoUrl = user.getPhotoUrl();
                    uid = user.getUid();

                    agendamento.setName(name);
                    agendamento.setUserID(uid);

                    String data = openCalendarField.getText().toString();
                    StringBuilder str = new StringBuilder(data);
                    str.insert(6, '2');
                    str.insert(7, '0');
                    agendamento.setData(str.toString());

                    if (photoUrl == null){
                        agendamento.setPhoto(AVATAR_IMAGE_URL);
                    } else {
                        agendamento.setPhoto(photoUrl.toString());
                    }

                    WriteBatch batch = mFirestore.batch();

                    if (t8.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b8.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t9.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b9.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t10.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b10.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t11.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b11.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t12.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b12.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t13.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b13.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t14.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b14.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t15.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b15.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t16.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b16.getText().toString());
                        batch.set(restRef, agendamento);
                    }
                    if (t17.getText().toString() == getResources().getString(R.string.selecao)) {
                        DocumentReference restRef = mFirestore.collection("agendamentos").document();
                        agendamento.setHora(b17.getText().toString());
                        batch.set(restRef, agendamento);
                    }

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Write batch succeeded.");
                                AlertDialog.Builder builder = new AlertDialog.Builder(AgendamentoActivity.this);
                                builder.setMessage("O agendamento foi realizado com sucesso!").setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AgendamentoActivity.this, ListagemActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setTitle("Confirmação");
                                alert.show();
                            } else {
                                Log.w(TAG, "write batch failed.", task.getException());
                            }
                        }
                    });

                }

            }
        });
    }
}
