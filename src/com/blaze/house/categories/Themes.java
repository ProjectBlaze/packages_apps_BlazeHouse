/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
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

package com.blaze.house.categories;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.blaze.BlazeUtils;
import com.blaze.house.preferences.SystemSettingListPreference;
import com.android.settings.R;
import android.provider.Settings;
import com.android.settings.SettingsPreferenceFragment;
import org.json.JSONException;
import org.json.JSONObject;
import static android.os.UserHandle.USER_SYSTEM;
import android.os.RemoteException;
import android.os.ServiceManager;
import static android.os.UserHandle.USER_CURRENT;

public class Themes extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "Themes";
    private static final String SETTINGS_DASHBOARD_STYLE = "settings_dashboard_style";
    private static final String CUSTOM_CLOCK_FACE = Settings.Secure.LOCK_SCREEN_CUSTOM_CLOCK_FACE;
    private static final String DEFAULT_CLOCK = "com.android.keyguard.clock.DefaultClockController";
    
    private SystemSettingListPreference mSettingsDashBoardStyle;
    private ListPreference mLockClockStyles;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.themes);

        ContentResolver resolver = getActivity().getContentResolver();
        
        mSettingsDashBoardStyle = (SystemSettingListPreference) findPreference(SETTINGS_DASHBOARD_STYLE);
        mSettingsDashBoardStyle.setOnPreferenceChangeListener(this);
        
        mLockClockStyles = (ListPreference) findPreference(CUSTOM_CLOCK_FACE);
        String mLockClockStylesValue = getLockScreenCustomClockFace();
        mLockClockStyles.setValue(mLockClockStylesValue);
        mLockClockStyles.setSummary(mLockClockStyles.getEntry());
        mLockClockStyles.setOnPreferenceChangeListener(this);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BLAZE_HOUSE;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        ContentResolver resolver = getActivity().getContentResolver();
	if (preference == mSettingsDashBoardStyle){
            BlazeUtils.showSettingsRestartDialog(getContext());
            return true;
            } else if (preference == mLockClockStyles) {
            setLockScreenCustomClockFace((String) objValue);
            int index = mLockClockStyles.findIndexOfValue((String) objValue);
            mLockClockStyles.setSummary(mLockClockStyles.getEntries()[index]);
            return true;
         }
        return false;
    }
    
    private String getLockScreenCustomClockFace() {
        mContext = getActivity();
        String value = Settings.Secure.getStringForUser(mContext.getContentResolver(),
                CUSTOM_CLOCK_FACE, USER_CURRENT);

        if (value == null || value.isEmpty()) value = DEFAULT_CLOCK;

        try {
            JSONObject json = new JSONObject(value);
            return json.getString("clock");
        } catch (JSONException ex) {
        }
        return value;
    }

    private void setLockScreenCustomClockFace(String value) {
        try {
            JSONObject json = new JSONObject();
            json.put("clock", value);
            Settings.Secure.putStringForUser(mContext.getContentResolver(), CUSTOM_CLOCK_FACE,
                    json.toString(), USER_CURRENT);
        } catch (JSONException ex) {
        }
    }
}
