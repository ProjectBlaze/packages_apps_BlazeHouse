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
import android.os.Bundle;
import android.content.res.Resources;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import android.provider.Settings;
import com.android.settings.SettingsPreferenceFragment;
import com.blaze.house.preferences.SecureSettingSwitchPreference;

public class StatusBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "StatusBar";
    private static final String COMBINED_STATUSBAR_ICONS = "combined_status_bar_signal_icons";
    private static final String CONFIG_RESOURCE_NAME = "flag_combined_status_bar_signal_icons";
    private static final String SYSTEMUI_PACKAGE = "com.android.systemui";

    SecureSettingSwitchPreference mCombinedIcons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.status_bar);

        PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mCombinedIcons = (SecureSettingSwitchPreference)
                findPreference(COMBINED_STATUSBAR_ICONS);
        Resources sysUIRes = null;
        boolean def = false;
        int resId = 0;
        try {
            sysUIRes = getActivity().getPackageManager()
                    .getResourcesForApplication(SYSTEMUI_PACKAGE);
        } catch (Exception ignored) {
            // If you don't have system UI you have bigger issues
        }
        if (sysUIRes != null) {
            resId = sysUIRes.getIdentifier(
                    CONFIG_RESOURCE_NAME, "bool", SYSTEMUI_PACKAGE);
            if (resId != 0) def = sysUIRes.getBoolean(resId);
        }
        mCombinedIcons.setChecked(Settings.Secure.getInt(getActivity().getContentResolver(), COMBINED_STATUSBAR_ICONS, def ? 1 : 0) == 1);
        mCombinedIcons.setOnPreferenceChangeListener(this);

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
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mCombinedIcons) {
            boolean enabled = (boolean) objValue;
            Settings.Secure.putInt(resolver,
                    COMBINED_STATUSBAR_ICONS, enabled ? 1 : 0);
            return true;
        }
        return true;
    }

}

