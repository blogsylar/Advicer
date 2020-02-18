package ru.zhdanov.advicer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Objects;

import ru.zhdanov.advicer.R;
import ru.zhdanov.advicer.Utils.NotificationReceiver;
import ru.zhdanov.advicer.Utils.Utils;

public class SettingsApp extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbar;
    private TextView tvName;
    private Utils utils;
    private Switch pushSwitch;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_settings_app);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        utils = new Utils(this);

        pushSwitch = findViewById(R.id.pushSwitch);
        pushSwitch.setChecked(utils.getPushSwitch());
        if (pushSwitch != null) pushSwitch.setOnCheckedChangeListener(this);

        tvName = findViewById(R.id.tvName);
        tvName.setOnClickListener(this);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        tvName.setText(utils.getLogin());
    }

    private void nameAlert() {
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(SettingsApp.this);
        nameDialog.setTitle("Ваше имя");

        final EditText nameInput = new EditText(SettingsApp.this);
        nameInput.setText(utils.getLogin());
        nameDialog.setView(nameInput);

        nameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                utils.setLogin(nameInput.getText().toString());
                tvName.setText(utils.getLogin());
            }
        });

        nameDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        nameDialog.show();
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvName:
                nameAlert();
                break;

            case R.id.logout:
                utils.setLogin("");
                cancelAlarm();
                startActivity(new Intent(this, Registration.class));
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        utils.setPushSwitch(isChecked);
    }
}
