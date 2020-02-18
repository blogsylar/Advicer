package ru.zhdanov.advicer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;

import ru.zhdanov.advicer.Api.AnswerCallback;
import ru.zhdanov.advicer.Api.ApiRequests;
import ru.zhdanov.advicer.R;
import ru.zhdanov.advicer.Utils.NotificationReceiver;
import ru.zhdanov.advicer.Utils.Utils;

public class MainActivity extends AppCompatActivity implements AnswerCallback {
    public static final String TAG = "happy";
    private ApiRequests apiRequests;
    private TextView tvAnswer;
    private Utils utils;
    private Toolbar toolbar;
    private Animation animationFadeIn;

    @Override
    protected void onStart() {
        super.onStart();
        checkNetwork();
        try {
            if (utils.getPushSwitch()) startAlarm();
            else cancelAlarm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        apiRequests = new ApiRequests();
        apiRequests.registerAnswerCallBacks(this);
        utils = new Utils(this);
        tvAnswer = findViewById(R.id.tvAnswer);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
    }

    private void startAlarm() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 12);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) c.add(Calendar.DATE, 1);
        if (alarmManager != null) alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 86400000, pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    private void checkNetwork() {
        if (utils.isOnline()) {
            request();
        } else {
            tvAnswer.setText(utils.getLogin() + ", подключись блять к интернету");
        }
    }

    private void request() {
        try {
            apiRequests.getAnswer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void answer(String text) {
        tvAnswer.setText(utils.getLogin() + ", " + text.substring(0, 1).toLowerCase() + text.substring(1));
        tvAnswer.startAnimation(animationFadeIn);
    }

    @Override
    public void exception(String text) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reload:
                checkNetwork();
                return true;

            case R.id.about:
                try {
                    startActivity(new Intent(this, About.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.settings:
                try {
                    startActivity(new Intent(this, SettingsApp.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
