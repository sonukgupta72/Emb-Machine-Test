package com.sonukgupta72.embibe.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sonukgupta72.embibe.R;
import com.sonukgupta72.embibe.fragment.DetailFragment;
import com.sonukgupta72.embibe.fragment.HomeFragment;
import com.sonukgupta72.embibe.receiver.AlarmReceiver;
import com.sonukgupta72.embibe.sqliteHelper.SQLiteHelperClass;

public class HomeActivity extends AppCompatActivity {

    public static final int FRAGMENT_HOME = 101;
    public static final int FRAGMENT_DETAILS = 102;
    private static final String HOME_TAG = "home";
    private static final String DETAILS_TAG = "details";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(FRAGMENT_HOME, null);
    }

    private boolean isDataAlreadyAdded() {
        SQLiteHelperClass sqLiteHelperClass = new SQLiteHelperClass(this);
        return sqLiteHelperClass.getAllMovieList().size() >= 100;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isDataAlreadyAdded()) {
            alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000 * 30,
                    1000 * 30, alarmIntent);
        }
    }

    public void changeFragment(int f, Bundle bundle) {
        Fragment fragment;
        switch (f) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                replaceFragment(fragment, HOME_TAG);
                break;
            case FRAGMENT_DETAILS:
                fragment = new DetailFragment();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
                replaceFragment(fragment, DETAILS_TAG);
                break;
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

}
