/*
 * Copyright (C) 2016 AospExtended ROM Project
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

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Handler;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.blaze.BlazeUtils;
import com.android.internal.util.blaze.ThemeUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.blaze.house.preferences.CustomSeekBarPreference;
import com.blaze.house.preferences.SystemSettingListPreference;

public class NotificationsPanel extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "NotificationsPanel";
    private static final String KEY_QS_UI_STYLE  = "qs_ui_style";

    private Handler mHandler;
    private SystemSettingListPreference mQsStyle;
    private SystemSettingListPreference mQsUI;
    private ThemeUtils mThemeUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notificationspanel);

        ContentResolver resolver = getActivity().getContentResolver();

	mQsUI = (SystemSettingListPreference) findPreference(KEY_QS_UI_STYLE);
    }

    private CustomSettingsObserver mCustomSettingsObserver = new CustomSettingsObserver(mHandler);
    private class CustomSettingsObserver extends ContentObserver {

        CustomSettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            Context mContext = getContext();
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(
                    Settings.System.QS_UI_STYLE),
                    false, this, UserHandle.USER_ALL);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(Settings.System.getUriFor(Settings.System.QS_UI_STYLE))) {
                updateQsStyle(true /*QS UI theme*/);
            }
        }
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
	if (preference == mQsUI) {
            mCustomSettingsObserver.observe();
            return true;
        }
        return true;
    }

	public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
		Settings.System.putIntForUser(resolver,
                Settings.System.QS_UI_STYLE, 0, UserHandle.USER_CURRENT);
    }

	private void updateQsStyle(boolean isQsUI) {
        ContentResolver resolver = getActivity().getContentResolver();

        boolean isA11Style = Settings.System.getIntForUser(getContext().getContentResolver(),
                Settings.System.QS_UI_STYLE , 1, UserHandle.USER_CURRENT) == 1;
	if (isQsUI) {
	    setQsStyle(isA11Style ? "com.android.system.qs.ui.A11" : "com.android.systemui");
		}
	}

	public void setQsStyle(String overlayName) {
		boolean isA11Style = Settings.System.getIntForUser(getContext().getContentResolver(),
                Settings.System.QS_UI_STYLE , 1, UserHandle.USER_CURRENT) == 1;
        mThemeUtils.setOverlayEnabled("android.theme.customization.qs_ui" , "com.android.systemui");
    }
}
