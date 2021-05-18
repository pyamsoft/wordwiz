/*
 * Copyright 2020 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.settings

import androidx.lifecycle.LifecycleObserver
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.pyamsoft.pydroid.arch.UiRender
import com.pyamsoft.pydroid.ui.arch.PrefUiView
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleLetterCount
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleWordCount
import javax.inject.Inject

internal class SettingsView @Inject internal constructor(preferenceScreen: PreferenceScreen) :
    PrefUiView<SettingsViewState, SettingsViewEvent>(preferenceScreen), LifecycleObserver {

  private val wordCount by boundPref<SwitchPreferenceCompat>(R.string.word_count_key)
  private val letterCount by boundPref<SwitchPreferenceCompat>(R.string.letter_count_key)

  init {
    doOnTeardown {
      wordCount.onPreferenceClickListener = null
      letterCount.onPreferenceClickListener = null
    }

    doOnInflate {
      wordCount.setOnPreferenceClickListener {
        publish(ToggleWordCount)
        return@setOnPreferenceClickListener true
      }
    }

    doOnInflate {
      letterCount.setOnPreferenceClickListener {
        publish(ToggleLetterCount)
        return@setOnPreferenceClickListener true
      }
    }
  }

  private fun handleWordCount(isEnabled: Boolean) {
    wordCount.isChecked = isEnabled
  }

  private fun handleLetterCount(isEnabled: Boolean) {
    letterCount.isChecked = isEnabled
  }

  override fun onRender(state: UiRender<SettingsViewState>) {
    state.mapChanged { it.isWordCountEnabled }.render(viewScope) { handleWordCount(it) }
    state.mapChanged { it.isLetterCountEnabled }.render(viewScope) { handleLetterCount(it) }
  }
}
