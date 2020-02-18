package ru.zhdanov.advicer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.zhdanov.advicer.R;
import ru.zhdanov.advicer.Utils.Utils;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private Button btnStart;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_registration);
        utils = new Utils(this);
        if (utils.getLogin().equals("")) {
            userName = findViewById(R.id.userName);
            btnStart = findViewById(R.id.btnStart);
            btnStart.setOnClickListener(this);
        } else {
            start();
        }
    }

    private void start() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Внимание! 18+")
                .setMessage("Возможна ненормативная лексика. Согласны?")
                .setCancelable(false)
                .setNegativeButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                utils.setLogin(userName.getText().toString());
                                start();
                            }
                        })
                .setPositiveButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if (userName.getText().toString().length() != 0) {
                    dialog();
                } else {
                    Toast.makeText(Registration.this, "Представьтесь, пожалуйста", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
