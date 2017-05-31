/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.main

import android.os.Bundle
import android.support.v7.preference.SwitchPreferenceCompat
import android.view.View
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.app.fragment.ActionBarSettingsPreferenceFragment
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWiz
import com.pyamsoft.wordwiz.word.count.LetterCountActivity
import com.pyamsoft.wordwiz.word.count.WordCountActivity

class MainPreferenceFragment : ActionBarSettingsPreferenceFragment() {

  override val isLastOnBackStack: AboutLibrariesFragment.BackStackState
    get() = AboutLibrariesFragment.BackStackState.LAST

  override val rootViewContainer: Int
    get() = R.id.main_view_container

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val preferenceXmlResId: Int
    get() = R.xml.preferences

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val wordCountPreference = findPreference(
        getString(R.string.word_count_key)) as SwitchPreferenceCompat
    wordCountPreference.isChecked = WordCountActivity.isEnabled(context)
    wordCountPreference.setOnPreferenceClickListener {
      val enabled = WordCountActivity.isEnabled(context)
      WordCountActivity.enable(context, !enabled)
      true
    }

    val letterCountPreference = findPreference(
        getString(R.string.letter_count_key)) as SwitchPreferenceCompat
    letterCountPreference.isChecked = LetterCountActivity.isEnabled(context)
    letterCountPreference.setOnPreferenceClickListener {
      val enabled = LetterCountActivity.isEnabled(context)
      LetterCountActivity.enable(context, !enabled)
      true
    }
  }

  override val hideClearAll: Boolean
    get() = true

  override fun onDestroy() {
    super.onDestroy()
    WordWiz.getRefWatcher(this).watch(this)
  }

  companion object {

    const val TAG = "MainPreferenceFragment"
  }
}
