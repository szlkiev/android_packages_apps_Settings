/*
 * Copyright (C) 2014 PacRoms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.pac.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Switch;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PacUiSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String UI_PAC_MODE = "pref_ui_pac_mode";
    private static final String UI_NIGHT_MODE = "pref_ui_night_mode";
    private static final String UI_NIGHT_AUTO_MODE = "pref_ui_night_auto_mode";

    private PacUiEnabler mPacUiEnabler;
    private ListPreference mUiPacMode;
    private CheckBoxPreference mUiNightMode;
    private ListPreference mUiNightAutoMode;

    private int mCurrentState = 0;
    private int mUiPac;
    private int mNightMode;
    private int mNightAutoMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pac_ui_settings);

        mUiPac = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_PAC, 2, UserHandle.USER_CURRENT);
        mNightMode = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_NIGHT_MODE, 1, UserHandle.USER_CURRENT);
        mNightAutoMode = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_NIGHT_AUTO_MODE, 2, UserHandle.USER_CURRENT);

        mUiPacMode = (ListPreference) findPreference(UI_PAC_MODE);
        int uiPacMode = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_PAC_MODE, 1, UserHandle.USER_CURRENT);
        mUiPacMode.setValue(String.valueOf(uiPacMode));
        mUiPacMode.setSummary(mUiPacMode.getEntry());
        mUiPacMode.setOnPreferenceChangeListener(this);

        mUiNightMode = (CheckBoxPreference) findPreference(UI_NIGHT_MODE);
        boolean uiNightMode = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_NIGHT_MODE, 1, UserHandle.USER_CURRENT) != 1;
        mUiNightMode.setChecked(uiNightMode);
        mUiNightMode.setOnPreferenceChangeListener(this);

        mUiNightAutoMode = (ListPreference) findPreference(UI_NIGHT_AUTO_MODE);
        int uiNightAutoMode = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.UI_NIGHT_AUTO_MODE, 2, UserHandle.USER_CURRENT);
        mUiNightAutoMode.setValue(String.valueOf(uiNightAutoMode));
        mUiNightAutoMode.setSummary(mUiNightAutoMode.getEntry());
        mUiNightAutoMode.setOnPreferenceChangeListener(this);

        final Activity activity = getActivity();
        final Switch actionBarSwitch = new Switch(activity);

        if (activity instanceof PreferenceActivity) {
            PreferenceActivity preferenceActivity = (PreferenceActivity) activity;
            if (preferenceActivity.onIsHidingHeaders() || !preferenceActivity.onIsMultiPane()) {
                final int padding = activity.getResources().getDimensionPixelSize(
                        R.dimen.action_bar_switch_padding);
                actionBarSwitch.setPaddingRelative(0, 0, padding, 0);
                activity.getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                        ActionBar.DISPLAY_SHOW_CUSTOM);
                activity.getActionBar().setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER_VERTICAL | Gravity.END));
            }
        }

        if (mUiPac != 2 && mPacUiEnabler != null) {
            return ;
        } else {
            mPacUiEnabler = new PacUiEnabler(activity, actionBarSwitch);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mUiPacMode) {
            int uiPacMode = Integer.valueOf((String) newValue);
            int index = mUiPacMode.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.UI_PAC_MODE, uiPacMode, UserHandle.USER_CURRENT);
            mUiPacMode.setSummary(mUiPacMode.getEntries()[index]);
            if (mUiPac != 2) {
                Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.UI_PAC, uiPacMode, UserHandle.USER_CURRENT);
            }
            return true;
        } else if (preference == mUiNightMode) {
            boolean uiNightMode = (Boolean) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.UI_NIGHT_MODE, uiNightMode ? mNightAutoMode : 1,
                    UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mUiNightAutoMode) {
            int uiNightAutoMode = Integer.valueOf((String) newValue);
            int index = mUiNightAutoMode.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.UI_NIGHT_AUTO_MODE, uiNightAutoMode, UserHandle.USER_CURRENT);
            mUiNightAutoMode.setSummary(mUiNightAutoMode.getEntries()[index]);
            if (mNightMode != 1) {
                Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.UI_NIGHT_MODE, uiNightAutoMode, UserHandle.USER_CURRENT);
            }
            return true;
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPacUiEnabler != null) {
            mPacUiEnabler.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPacUiEnabler != null) {
            mPacUiEnabler.pause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.uiPac != mCurrentState && mPacUiEnabler != null) {
            mCurrentState = newConfig.uiPac;
        }
    }
}
