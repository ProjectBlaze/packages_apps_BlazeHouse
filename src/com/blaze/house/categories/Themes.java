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
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.blaze.BlazeUtils;
import com.blaze.house.preferences.SystemSettingListPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Themes extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "Themes";
    private static final String SETTINGS_DASHBOARD_STYLE = "settings_dashboard_style";
    
    private SystemSettingListPreference mSettingsDashBoardStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.themes);

        ContentResolver resolver = getActivity().getContentResolver();
        
        mSettingsDashBoardStyle = (SystemSettingListPreference) findPreference(SETTINGS_DASHBOARD_STYLE);
        mSettingsDashBoardStyle.setOnPreferenceChangeListener(this);
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
            }
        return false;
    }
}