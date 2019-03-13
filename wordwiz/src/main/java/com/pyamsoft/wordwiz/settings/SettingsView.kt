/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.wordwiz.settings

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.pyamsoft.pydroid.ui.arch.PrefUiView
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity

internal class SettingsView internal constructor(
  preferenceScreen: PreferenceScreen,
  callback: SettingsView.Callback
) : PrefUiView<SettingsView.Callback>(preferenceScreen, callback), LifecycleObserver {

  private val wordCount by lazyPref<SwitchPreferenceCompat>(R.string.word_count_key)
  private val letterCount by lazyPref<SwitchPreferenceCompat>(R.string.letter_count_key)

  override fun onInflated(
    preferenceScreen: PreferenceScreen,
    savedInstanceState: Bundle?
  ) {
    setupWordCount()
    setupLetterCount()
  }

  override fun onTeardown() {
    wordCount.onPreferenceClickListener = null
    letterCount.onPreferenceClickListener = null
  }

  private fun setupWordCount() {
    wordCount.setOnPreferenceClickListener {
      val enabled = WordCountActivity.isEnabled(wordCount.context)
      callback.onWordCountToggled(!enabled)
      syncState()

      return@setOnPreferenceClickListener true
    }
  }

  private fun setupLetterCount() {
    letterCount.setOnPreferenceClickListener {
      val enabled = LetterCountActivity.isEnabled(letterCount.context)
      callback.onLetterCountToggled(!enabled)
      syncState()

      return@setOnPreferenceClickListener true
    }
  }

  fun syncState() {
    wordCount.isChecked = WordCountActivity.isEnabled(wordCount.context)
    letterCount.isChecked = LetterCountActivity.isEnabled(letterCount.context)
  }

  interface Callback {

    fun onWordCountToggled(enabled: Boolean)

    fun onLetterCountToggled(enabled: Boolean)
  }

}
