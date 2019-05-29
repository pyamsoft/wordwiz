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

import androidx.lifecycle.LifecycleObserver
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.pyamsoft.pydroid.arch.UiSavedState
import com.pyamsoft.pydroid.ui.arch.PrefUiView
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleLetterCount
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleWordCount
import javax.inject.Inject

internal class SettingsView @Inject internal constructor(
  preferenceScreen: PreferenceScreen
) : PrefUiView<SettingsViewState, SettingsViewEvent>(preferenceScreen),
    LifecycleObserver {

  private val wordCount by boundPref<SwitchPreferenceCompat>(R.string.word_count_key)
  private val letterCount by boundPref<SwitchPreferenceCompat>(R.string.letter_count_key)

  override fun onRender(
    state: SettingsViewState,
    savedState: UiSavedState
  ) {
    state.isWordCountEnabled.let { enabled ->
      wordCount.isChecked = enabled
      wordCount.setOnPreferenceClickListener {
        publish(ToggleWordCount(!enabled))
        return@setOnPreferenceClickListener true
      }
    }

    state.isLetterCountEnabled.let { enabled ->
      letterCount.isChecked = enabled
      letterCount.setOnPreferenceClickListener {
        publish(ToggleLetterCount(!enabled))
        return@setOnPreferenceClickListener true
      }
    }
  }

  override fun onTeardown() {
    wordCount.onPreferenceClickListener = null
    letterCount.onPreferenceClickListener = null
  }

}
