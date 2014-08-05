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

import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PacUiEnabler implements CompoundButton.OnCheckedChangeListener {
    private final Context mContext;
    private Switch mSwitch;
    private boolean mStateMachineEvent;

    public PacUiEnabler(Context context, Switch switch_) {
        mContext = context;
        mSwitch = switch_;
    }

    public void resume() {
        mSwitch.setOnCheckedChangeListener(this);
        setSwitchState();
    }

    public void pause() {
        mSwitch.setOnCheckedChangeListener(null);
    }

    public void setSwitch(Switch switch_) {
        if (mSwitch == switch_) return;
        mSwitch.setOnCheckedChangeListener(null);
        mSwitch = switch_;
        mSwitch.setOnCheckedChangeListener(this);
        setSwitchState();
    }

    public void setSwitchState() {
        boolean state = Settings.Secure.getInt(mContext.getContentResolver(),
                Settings.Secure.UI_PAC, 2) != 2;
        mStateMachineEvent = true;
        mSwitch.setChecked(state);
        mStateMachineEvent = false;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mStateMachineEvent) {
            return;
        }
        // Handle a switch change
        Settings.Secure.putInt(mContext.getContentResolver(),
                Settings.Secure.UI_PAC, isChecked ?
                    Settings.Secure.getInt(mContext.getContentResolver(),
                    Settings.Secure.UI_PAC_MODE, 1) : 2);
        setSwitchState();
    }
}
