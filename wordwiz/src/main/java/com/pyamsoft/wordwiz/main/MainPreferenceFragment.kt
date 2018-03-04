/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.wordwiz.main

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.SwitchPreferenceCompat
import android.view.View
import com.pyamsoft.pydroid.ui.app.fragment.SettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.util.setUpEnabled
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWiz
import com.pyamsoft.wordwiz.word.count.LetterCountActivity
import com.pyamsoft.wordwiz.word.count.WordCountActivity

class MainPreferenceFragment : SettingsPreferenceFragment() {

  private lateinit var wordCountPreference: SwitchPreferenceCompat
  private lateinit var letterCountPreference: SwitchPreferenceCompat

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val rootViewContainer: Int = R.id.main_view_container

  override val preferenceXmlResId: Int = R.xml.preferences

  override val hideClearAll: Boolean = true

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    val context: Context = view.context
    wordCountPreference =
        findPreference(getString(R.string.word_count_key)) as SwitchPreferenceCompat
    wordCountPreference.setOnPreferenceClickListener {
      val enabled = WordCountActivity.isEnabled(context)
      WordCountActivity.enable(context, !enabled)
      return@setOnPreferenceClickListener true
    }

    letterCountPreference =
        findPreference(getString(R.string.letter_count_key)) as SwitchPreferenceCompat
    letterCountPreference.setOnPreferenceClickListener {
      val enabled = LetterCountActivity.isEnabled(context)
      LetterCountActivity.enable(context, !enabled)
      return@setOnPreferenceClickListener true
    }
  }

  override fun onStart() {
    super.onStart()
    context?.let {
      wordCountPreference.isChecked = WordCountActivity.isEnabled(it)
      letterCountPreference.isChecked = LetterCountActivity.isEnabled(it)
    }
  }

  override fun onResume() {
    super.onResume()
    toolbarActivity.withToolbar {
      it.setTitle(R.string.app_name)
      it.setUpEnabled(false)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    WordWiz.getRefWatcher(this)
        .watch(this)
  }

  companion object {

    const val TAG = "MainPreferenceFragment"
  }
}
