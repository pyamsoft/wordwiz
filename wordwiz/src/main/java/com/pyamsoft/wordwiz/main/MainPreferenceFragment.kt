/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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

  private lateinit var wordCountPreference: SwitchPreferenceCompat
  private lateinit var letterCountPreference: SwitchPreferenceCompat

  override val isLastOnBackStack: AboutLibrariesFragment.BackStackState
    get() = AboutLibrariesFragment.BackStackState.LAST

  override val rootViewContainer: Int
    get() = R.id.main_view_container

  override val applicationName: String
    get() = getString(R.string.app_name)

  override val preferenceXmlResId: Int
    get() = R.xml.preferences

  override val hideClearAll: Boolean
    get() = true

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    wordCountPreference = findPreference(
        getString(R.string.word_count_key)) as SwitchPreferenceCompat
    wordCountPreference.setOnPreferenceClickListener {
      val enabled = WordCountActivity.isEnabled(context)
      WordCountActivity.enable(context, !enabled)
      return@setOnPreferenceClickListener true
    }

    letterCountPreference = findPreference(
        getString(R.string.letter_count_key)) as SwitchPreferenceCompat
    letterCountPreference.setOnPreferenceClickListener {
      val enabled = LetterCountActivity.isEnabled(context)
      LetterCountActivity.enable(context, !enabled)
      return@setOnPreferenceClickListener true
    }
  }

  override fun onStart() {
    super.onStart()
    wordCountPreference.isChecked = WordCountActivity.isEnabled(context)
    letterCountPreference.isChecked = LetterCountActivity.isEnabled(context)
  }

  override fun onResume() {
    super.onResume()
    setActionBarTitle(R.string.app_name)
  }

  override fun onDestroy() {
    super.onDestroy()
    WordWiz.getRefWatcher(this).watch(this)
  }

  companion object {

    const val TAG = "MainPreferenceFragment"
  }
}
