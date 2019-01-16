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

package com.pyamsoft.wordwiz

import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceScreen
import com.pyamsoft.wordwiz.main.MainActivity
import com.pyamsoft.wordwiz.main.MainComponent
import com.pyamsoft.wordwiz.settings.SettingsComponent
import com.pyamsoft.wordwiz.word.WordProcessActivity

interface WordWizComponent {

  fun inject(activity: WordProcessActivity)

  @CheckResult
  fun plusMainComponent(
    parent: ViewGroup,
    owner: LifecycleOwner
  ): MainComponent

  @CheckResult
  fun plusSettingsComponent(
    owner: LifecycleOwner,
    preferenceScreen: PreferenceScreen
  ): SettingsComponent
}
