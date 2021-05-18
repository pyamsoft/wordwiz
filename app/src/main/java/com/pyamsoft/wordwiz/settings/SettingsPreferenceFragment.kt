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

import android.os.Bundle
import android.view.View
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.UiController
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.arch.fromViewModelFactory
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.WordWizViewModelFactory
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity
import javax.inject.Inject

class SettingsPreferenceFragment :
    AppSettingsPreferenceFragment(), UiController<SettingsControllerEvent> {

  @JvmField @Inject internal var factory: WordWizViewModelFactory? = null
  private val viewModel by fromViewModelFactory<SettingsViewModel> { factory?.create(this) }

  @JvmField @Inject internal var settingsView: SettingsView? = null

  @JvmField @Inject internal var spacer: SettingsSpacer? = null

  @JvmField @Inject internal var toolbar: SettingsToolbarView? = null

  private var stateSaver: StateSaver? = null

  override val preferenceXmlResId: Int = R.xml.preferences

  override val hideClearAll: Boolean = true

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    Injector.obtainFromApplication<WordWizComponent>(view.context)
        .plusSettingsComponent()
        .create(requireToolbarActivity(), preferenceScreen)
        .inject(this)

    stateSaver =
        createComponent(
            savedInstanceState,
            viewLifecycleOwner,
            viewModel,
            this,
            requireNotNull(settingsView),
            requireNotNull(toolbar),
            requireNotNull(spacer),
        ) {
          return@createComponent when (it) {
            is SettingsViewEvent.ToggleLetterCount -> viewModel.handleLetterCountToggled()
            is SettingsViewEvent.ToggleWordCount -> viewModel.handleWordCountToggled()
          }
        }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    stateSaver?.saveState(outState)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    stateSaver = null
    factory = null
    settingsView = null
    toolbar = null
    factory = null
  }

  private fun onWordCountChanged(enabled: Boolean) {
    WordCountActivity.enable(requireContext(), enabled)
  }

  private fun onLetterCountChanged(enabled: Boolean) {
    LetterCountActivity.enable(requireContext(), enabled)
  }

  override fun onControllerEvent(event: SettingsControllerEvent) {
    return when (event) {
      is SettingsControllerEvent.LetterCountToggled -> onLetterCountChanged(event.enabled)
      is SettingsControllerEvent.WordCountToggled -> onWordCountChanged(event.enabled)
    }
  }

  companion object {

    const val TAG = "SettingsPreferenceFragment"
  }
}
