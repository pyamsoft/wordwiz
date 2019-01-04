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

package com.pyamsoft.wordwiz.main

import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.preference.SwitchPreferenceCompat
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity

internal class MainPrefViewImpl internal constructor(
  private val preferenceScreen: PreferenceScreen,
  private val owner: LifecycleOwner
) : MainPrefView, LifecycleObserver {

  private val context = preferenceScreen.context
  private lateinit var wordCountPreference: SwitchPreferenceCompat
  private lateinit var letterCountPreference: SwitchPreferenceCompat

  init {
    owner.lifecycle.addObserver(this)
  }

  override fun create() {
    wordCountPreference = findPreference(R.string.word_count_key) as SwitchPreferenceCompat
    letterCountPreference = findPreference(R.string.letter_count_key) as SwitchPreferenceCompat
  }

  @Suppress("unused")
  @OnLifecycleEvent(ON_DESTROY)
  internal fun destroy() {
    owner.lifecycle.removeObserver(this)
  }

  @CheckResult
  private fun findPreference(@StringRes key: Int): Preference {
    return preferenceScreen.findPreference(context.getString(key))
  }

  override fun onWordCountClicked(onClick: () -> Unit) {
    wordCountPreference.setOnPreferenceClickListener {
      onClick()
      return@setOnPreferenceClickListener true
    }
  }

  override fun onLetterCountClicked(onClick: () -> Unit) {
    letterCountPreference.setOnPreferenceClickListener {
      onClick()
      return@setOnPreferenceClickListener true
    }
  }

  @Suppress("unused")
  @OnLifecycleEvent(ON_START)
  internal fun syncState() {
    wordCountPreference.isChecked = WordCountActivity.isEnabled(context)
    letterCountPreference.isChecked = LetterCountActivity.isEnabled(context)
  }

}