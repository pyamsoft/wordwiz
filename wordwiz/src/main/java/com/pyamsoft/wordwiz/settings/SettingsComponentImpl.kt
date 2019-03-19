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

import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.wordwiz.widget.ToolbarView

internal class SettingsComponentImpl internal constructor(
  private val preferenceScreen: PreferenceScreen
) : SettingsComponent {

  override fun inject(fragment: SettingsPreferenceFragment) {
    val settingsView = SettingsView(preferenceScreen, fragment)

    fragment.apply {
      this.settingsView = settingsView
      this.toolbarView = ToolbarView(fragment.requireToolbarActivity())
    }
  }

}