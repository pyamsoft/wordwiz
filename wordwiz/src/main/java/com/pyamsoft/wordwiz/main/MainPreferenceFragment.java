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

package com.pyamsoft.wordwiz.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;
import com.pyamsoft.pydroidui.about.AboutLibrariesFragment;
import com.pyamsoft.pydroidui.app.fragment.ActionBarSettingsPreferenceFragment;
import com.pyamsoft.wordwiz.R;
import com.pyamsoft.wordwiz.WordWiz;
import com.pyamsoft.wordwiz.word.count.LetterCountActivity;
import com.pyamsoft.wordwiz.word.count.WordCountActivity;

public class MainPreferenceFragment extends ActionBarSettingsPreferenceFragment {

  @NonNull public static final String TAG = "MainPreferenceFragment";

  @NonNull @Override protected AboutLibrariesFragment.BackStackState isLastOnBackStack() {
    return AboutLibrariesFragment.BackStackState.LAST;
  }

  @Override protected int getRootViewContainer() {
    return R.id.main_view_container;
  }

  @NonNull @Override protected String getApplicationName() {
    return getString(R.string.app_name);
  }

  @Override protected int getPreferenceXmlResId() {
    return R.xml.preferences;
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

    final SwitchPreferenceCompat letterCountPreference =
        (SwitchPreferenceCompat) findPreference(getString(R.string.letter_count_key));
    letterCountPreference.setChecked(LetterCountActivity.isEnabled(getContext()));
    letterCountPreference.setOnPreferenceClickListener(preference -> {
      final boolean enabled = LetterCountActivity.isEnabled(getContext());
      LetterCountActivity.enable(getContext(), !enabled);
      return true;
    });
  }

  @Override protected boolean hideClearAll() {
    return true;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    WordWiz.getRefWatcher(this).watch(this);
  }
}
