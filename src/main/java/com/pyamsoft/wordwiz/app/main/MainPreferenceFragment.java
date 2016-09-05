/*
 * Copyright 2016 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.wordwiz.app.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;
import com.pyamsoft.pydroid.about.AboutLibrariesFragment;
import com.pyamsoft.pydroid.app.fragment.ActionBarSettingsPreferenceFragment;
import com.pyamsoft.pydroid.model.Licenses;
import com.pyamsoft.wordwiz.R;
import com.pyamsoft.wordwiz.app.word.count.WordCountActivity;

public class MainPreferenceFragment extends ActionBarSettingsPreferenceFragment {

  @NonNull public static final String TAG = "MainPreferenceFragment";

  @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final SwitchPreferenceCompat wordCountPreference =
        (SwitchPreferenceCompat) findPreference(getString(R.string.word_count_key));
    wordCountPreference.setChecked(WordCountActivity.isEnabled(getContext()));
    wordCountPreference.setOnPreferenceClickListener(preference -> {
      final boolean enabled = WordCountActivity.isEnabled(getContext());
      WordCountActivity.enable(getContext(), !enabled);
      return true;
    });

    final SwitchPreferenceCompat showAds =
        (SwitchPreferenceCompat) findPreference(getString(R.string.adview_key));
    showAds.setOnPreferenceChangeListener((preference, newValue) -> toggleAdVisibility(newValue));

    final Preference showAboutLicenses = findPreference(getString(R.string.about_license_key));
    showAboutLicenses.setOnPreferenceClickListener(
        preference -> showAboutLicensesFragment(R.id.main_view_container,
            AboutLibrariesFragment.Styling.LIGHT, Licenses.ANDROID, Licenses.PYDROID));

    final Preference checkVersion = findPreference(getString(R.string.check_version_key));
    checkVersion.setOnPreferenceClickListener(preference -> checkForUpdate());

    //final Preference upgradeInfo = findPreference(getString(R.string.upgrade_info_key));
    //upgradeInfo.setOnPreferenceClickListener(preference -> showChangelog());
  }
}
