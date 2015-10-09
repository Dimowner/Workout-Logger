package ua.com.sofon.workoutlogger.ui;

/*
 * Copyright 2014 Google Inc. All rights reserved.
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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.PrefUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

/**
 * Activity for customizing app settings.
 */
public class SettingsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.title_settings);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_left_grey600_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				navigateUpToFromChild(SettingsActivity.this,
						IntentCompat.makeMainActivity(new ComponentName(SettingsActivity.this,
								WorkoutsActivity.class)));
			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SettingsFragment())
					.commit();
		}
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_SETTINGS;
	}

	public static class SettingsFragment extends PreferenceFragment
			implements SharedPreferences.OnSharedPreferenceChangeListener {
		public SettingsFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setupSimplePreferencesScreen();
            PrefUtils.registerOnSharedPreferenceChangeListener(getActivity(), this);
			Preference prefAbout = findPreference("about");
			prefAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					FragmentManager fm = getActivity().getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					Fragment prev = fm.findFragmentByTag("dialog_about");
					if (prev != null) {
						ft.remove(prev);
					}
					ft.addToBackStack(null);
					new AboutDialog().show(ft, "dialog_about");
					return false;
				}
			});
//			Preference prefLogs = findPreference("logs");
//			prefLogs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//				@Override
//				public boolean onPreferenceClick(Preference preference) {
//					LOGV("SettingFragment", "startLogsActivity");
//					startActivity(new Intent(getActivity(), LogsActivity.class));
//					return false;
//				}
//			});
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
            PrefUtils.unregisterOnSharedPreferenceChangeListener(getActivity(), this);
		}

		private void setupSimplePreferencesScreen() {
			// Add 'general' preferences.
			addPreferencesFromResource(R.xml.preferences);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			LOGV("SettingsActivity", "onPreferenceChanged");
		}

//		Preference prefAbout;
	}
}
