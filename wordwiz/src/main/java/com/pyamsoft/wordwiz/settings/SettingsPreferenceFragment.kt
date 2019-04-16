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
import android.view.View
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity
import javax.inject.Inject

class SettingsPreferenceFragment : AppSettingsPreferenceFragment(), SettingsUiComponent.Callback {

  @field:Inject internal lateinit var component: SettingsUiComponent

  override val preferenceXmlResId: Int = R.xml.preferences

  override val hideClearAll: Boolean = true

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    Injector.obtain<WordWizComponent>(requireContext().applicationContext)
        .plusSettingsComponent()
        .create(requireToolbarActivity(), preferenceScreen)
        .inject(this)

    component.bind(viewLifecycleOwner, savedInstanceState, this)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    component.saveState(outState)
  }

  override fun onStart() {
    super.onStart()
    component.sync()
  }

  override fun onWordCountChanged(enabled: Boolean) {
    WordCountActivity.enable(requireContext(), enabled)
  }

  override fun onLetterCountChanged(enabled: Boolean) {
    LetterCountActivity.enable(requireContext(), enabled)
  }

  companion object {

    const val TAG = "SettingsPreferenceFragment"
  }
}
