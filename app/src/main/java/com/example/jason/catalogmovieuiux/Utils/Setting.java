package com.example.jason.catalogmovieuiux.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jason.catalogmovieuiux.R;
import com.example.jason.catalogmovieuiux.scheduler.AlarmReceiver;
import com.example.jason.catalogmovieuiux.scheduler.SchedulerTask;

import butterknife.ButterKnife;

public class Setting extends AppCompatActivity {

    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    private SchedulerTask schedulerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @SuppressLint("ValidFragment")
    private class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener{

        String reminder_daily, reminder_upcoming, setting_locale;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            ButterKnife.bind(this, getActivity());

            reminder_daily = getResources().getString(R.string.key_reminder_daily);
            reminder_upcoming = getResources().getString(R.string.key_reminder_upcoming);
            setting_locale = getResources().getString(R.string.key_setting_locale);

            findPreference(reminder_daily).setOnPreferenceChangeListener(this);
            findPreference(reminder_upcoming).setOnPreferenceChangeListener(this);
            findPreference(setting_locale).setOnPreferenceClickListener(this);

            schedulerTask = new SchedulerTask(getActivity());
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String key = preference.getKey();
            boolean on = (boolean) o;

            if (key.equals(reminder_daily)) {
                if (on) {
                    alarmReceiver.setRepeatingAlarm(Setting.this, alarmReceiver.TYPE_REPEATING, "07:00", getResources().getString(R.string.alarmMessage));
                } else {
                    alarmReceiver.cancelAlarm(getActivity(), alarmReceiver.TYPE_REPEATING);
                }

                Toast.makeText(Setting.this, getString(R.string.alarmnotif) + " " + (on ? getString(R.string.actived)  : getString(R.string.deactivated)), Toast.LENGTH_SHORT).show();
                return true;
            }

            if (key.equals(reminder_upcoming)) {
                if (on) {
                    schedulerTask.createPeriodicTask();
                } else schedulerTask.cancelPeriodicTask();

                Toast.makeText(Setting.this, getString(R.string.upcomingnotif) + " " + (on ? getString(R.string.actived) : getString(R.string.deactivated)), Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(setting_locale)) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            }

            return false;
        }
    }
}
