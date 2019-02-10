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
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.pyamsoft.pydroid.ui.arch.PrefUiView
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity

internal class SettingsView internal constructor(
  private val owner: LifecycleOwner,
  preferenceScreen: PreferenceScreen,
  callback: SettingsView.Callback
) : PrefUiView<SettingsView.Callback>(preferenceScreen, callback), LifecycleObserver {

  private val context = parent.context
  private lateinit var wordCount: SwitchPreferenceCompat
  private lateinit var letterCount: SwitchPreferenceCompat

  override fun inflate(savedInstanceState: Bundle?) {
    owner.lifecycle.addObserver(object : LifecycleObserver {

      @Suppress("unused")
      @OnLifecycleEvent(ON_START)
      fun onStart() {
        syncState()
      }

      @Suppress("unused")
      @OnLifecycleEvent(ON_DESTROY)
      fun destroy() {
        owner.lifecycle.removeObserver(this)
      }

    })

    wordCount = findPreference(R.string.word_count_key) as SwitchPreferenceCompat
    letterCount = findPreference(R.string.letter_count_key) as SwitchPreferenceCompat

    setupWordCount()
    setupLetterCount()
  }

  override fun teardown() {
    wordCount.onPreferenceClickListener = null
    letterCount.onPreferenceClickListener = null
  }

  @CheckResult
  private fun findPreference(@StringRes key: Int): Preference {
    return parent.findPreference(context.getString(key))
  }

  private fun setupWordCount() {
    wordCount.setOnPreferenceClickListener {
      val enabled = WordCountActivity.isEnabled(context)
      callback.onWordCountToggled(!enabled)
      syncState()

      return@setOnPreferenceClickListener true
    }
  }

  private fun setupLetterCount() {
    letterCount.setOnPreferenceClickListener {
      val enabled = LetterCountActivity.isEnabled(context)
      callback.onLetterCountToggled(!enabled)
      syncState()

      return@setOnPreferenceClickListener true
    }
  }

  private fun syncState() {
    wordCount.isChecked = WordCountActivity.isEnabled(context)
    letterCount.isChecked = LetterCountActivity.isEnabled(context)
  }

  interface Callback {

    fun onWordCountToggled(enabled: Boolean)

    fun onLetterCountToggled(enabled: Boolean)
  }

}
