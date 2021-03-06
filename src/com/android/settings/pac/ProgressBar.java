/*
 * Copyright (C) 2013-2014 Dokdo Project - Gwon Hyeok
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

package com.android.settings.pac;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.R;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.android.settings.SettingsPreferenceFragment;

public class ProgressBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static PreferenceCategory PreviewLayout;
    private static final String PROGRESSBAR_MIRROR = "progressbar_mirror";
    private static final String PROGRESSBAR_REVERSE = "progressbar_reverse";
    private static final String PROGRESSBAR_SPEED = "progressbar_speed";
    private static final String PROGRESSBAR_WIDTH = "progressbar_width";
    private static final String PROGRESSBAR_LENGTH = "progressbar_length";
    private static final String PROGRESSBAR_COUNT = "progressbar_count";
    private static final String PROGRESSBAR_COLOR_1 = "progressbar_color_1";
    private static final String PROGRESSBAR_COLOR_2 = "progressbar_color_2";
    private static final String PROGRESSBAR_COLOR_3 = "progressbar_color_3";
    private static final String PROGRESSBAR_COLOR_4 = "progressbar_color_4";

    private CheckBoxPreference mprogressbar_mirror;
    private CheckBoxPreference mprogressbar_reverse;
    private SeekBarPreferenceCHOS mprogressbar_speed;
    private SeekBarPreferenceCHOS mprogressbar_width;
    private SeekBarPreferenceCHOS mprogressbar_length;
    private SeekBarPreferenceCHOS mprogressbar_count;
    private ColorPickerPreference mprogressbar_color_1;
    private ColorPickerPreference mprogressbar_color_2;
    private ColorPickerPreference mprogressbar_color_3;
    private ColorPickerPreference mprogressbar_color_4;

    private static final int MENU_RESET = Menu.FIRST;
    private static final int defaultColorOne = 0xffffff00;
    private static final int defaultColorTwo = 0xff99cc00;
    private static final int defaultColorThree = 0xffaa66cc;
    private static final int defaultColorFour = 0xff00ddff;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.progressbar);
        PreviewLayout = new PreferenceCategory(getActivity());
        PreviewLayout.setLayoutResource(R.layout.preview_progressbar);
        getPreferenceScreen().removeAll();
        getPreferenceScreen().addPreference(PreviewLayout);
        addPreferencesFromResource(R.xml.progressbar);

        mprogressbar_mirror = (CheckBoxPreference) findPreference(PROGRESSBAR_MIRROR);
        mprogressbar_mirror.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_MIRROR, 0) == 1);

        mprogressbar_reverse = (CheckBoxPreference) findPreference(PROGRESSBAR_REVERSE);
        mprogressbar_reverse.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_REVERSE, 0) == 1);

        mprogressbar_speed = (SeekBarPreferenceCHOS) findPreference(PROGRESSBAR_SPEED);
        mprogressbar_speed.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.PROGRESSBAR_SPEED, 4));
        mprogressbar_speed.setOnPreferenceChangeListener(this);

        mprogressbar_width = (SeekBarPreferenceCHOS) findPreference(PROGRESSBAR_WIDTH);
        mprogressbar_width.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.PROGRESSBAR_WIDTH, 4));
        mprogressbar_width.setOnPreferenceChangeListener(this);

        mprogressbar_length = (SeekBarPreferenceCHOS) findPreference(PROGRESSBAR_LENGTH);
        mprogressbar_length.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.PROGRESSBAR_LENGTH, 10));
        mprogressbar_length.setOnPreferenceChangeListener(this);

        mprogressbar_count = (SeekBarPreferenceCHOS) findPreference(PROGRESSBAR_COUNT);
        mprogressbar_count.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.PROGRESSBAR_COUNT, 6));
        mprogressbar_count.setOnPreferenceChangeListener(this);

        mprogressbar_color_1 = (ColorPickerPreference) findPreference(PROGRESSBAR_COLOR_1);
        int intColor1 = Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_1, defaultColorOne);
        mprogressbar_color_1.setNewPreviewColor(intColor1);
        mprogressbar_color_1.setOnPreferenceChangeListener(this);

        mprogressbar_color_2 = (ColorPickerPreference) findPreference(PROGRESSBAR_COLOR_2);
        int intColor2 = Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_2, defaultColorTwo);
        mprogressbar_color_2.setNewPreviewColor(intColor2);
        mprogressbar_color_2.setOnPreferenceChangeListener(this);

        mprogressbar_color_3 = (ColorPickerPreference) findPreference(PROGRESSBAR_COLOR_3);
        int intColor3 = Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_3, defaultColorThree);
        mprogressbar_color_3.setNewPreviewColor(intColor3);
        mprogressbar_color_3.setOnPreferenceChangeListener(this);

        mprogressbar_color_4 = (ColorPickerPreference) findPreference(PROGRESSBAR_COLOR_4);
        int intColor4 = Settings.System.getInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_4, defaultColorFour);
        mprogressbar_color_4.setNewPreviewColor(intColor4);
        mprogressbar_color_4.setOnPreferenceChangeListener(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.ram_bar_button_reset)
                .setIcon(R.drawable.ic_settings_backup)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.ram_bar_reset);
        alertDialog.setMessage(R.string.progressbar_reset_message);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ProgressBarColorReset();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    }

    private void ProgressBarColorReset() {
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_MIRROR, 0);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_REVERSE, 0);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_SPEED, 4);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_WIDTH, 4);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_LENGTH, 10);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_COUNT, 6);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_1, defaultColorOne);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_2, defaultColorTwo);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_3, defaultColorThree);
        Settings.System.putInt(getContentResolver(), Settings.System.PROGRESSBAR_COLOR_4, defaultColorFour);

        mprogressbar_mirror.setChecked(false);
        mprogressbar_reverse.setChecked(false);
        mprogressbar_speed.setValue(4);
        mprogressbar_width.setValue(4);
        mprogressbar_length.setValue(10);
        mprogressbar_count.setValue(6);
        mprogressbar_color_1.setNewPreviewColor(defaultColorOne);
        mprogressbar_color_2.setNewPreviewColor(defaultColorTwo);
        mprogressbar_color_3.setNewPreviewColor(defaultColorThree);
        mprogressbar_color_4.setNewPreviewColor(defaultColorFour);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
   public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mprogressbar_mirror) {
            value = mprogressbar_mirror.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_MIRROR, value ? 1 : 0);
            return true;
        } else if (preference == mprogressbar_reverse) {
            value = mprogressbar_reverse.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_REVERSE, value ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mprogressbar_speed) {
            int speed = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_SPEED, speed);
            return true;
        } else if ( preference == mprogressbar_width) {
            int width = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                Settings.System.PROGRESSBAR_WIDTH, width);
            return true;
        } else if ( preference == mprogressbar_length) {
            int length = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_LENGTH, length);
            return true;
        } else if ( preference == mprogressbar_count) {
            int count = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_COUNT, count);
            return true;
        } else if ( preference == mprogressbar_color_1) {
            int color1 = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_COLOR_1, color1);
            return true;
        } else if ( preference == mprogressbar_color_2) {
            int color2 = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_COLOR_2, color2);
            return true;
        } else if ( preference == mprogressbar_color_3) {
            int color3 = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_COLOR_3, color3);
            return true;
        } else if ( preference == mprogressbar_color_4) {
            int color4 = ((Integer)newValue).intValue();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.PROGRESSBAR_COLOR_4, color4);
            return true;
        }
        return false;
    }
}
