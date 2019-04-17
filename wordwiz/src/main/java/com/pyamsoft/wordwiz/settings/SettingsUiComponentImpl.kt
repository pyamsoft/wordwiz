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
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.arch.renderOnChange
import com.pyamsoft.pydroid.ui.arch.InvalidIdException
import com.pyamsoft.wordwiz.settings.SettingsUiComponent.Callback
import com.pyamsoft.wordwiz.settings.SettingsViewModel.SettingsState
import javax.inject.Inject

internal class SettingsUiComponentImpl @Inject internal constructor(
  private val toolbarView: SettingsToolbarView,
  private val settingsView: SettingsView,
  private val viewModel: SettingsViewModel
) : BaseUiComponent<Callback>(),
    SettingsUiComponent {

  override fun id(): Int {
    throw InvalidIdException
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Callback
  ) {
    owner.doOnDestroy {
      settingsView.teardown()
      toolbarView.teardown()
      viewModel.unbind()
    }

    settingsView.inflate(savedInstanceState)
    toolbarView.inflate(savedInstanceState)
    viewModel.bind { state, oldState ->
      renderWordCount(state, oldState)
      renderLetterCount(state, oldState)
    }
  }

  private fun renderWordCount(
    state: SettingsState,
    oldState: SettingsState?
  ) {
    state.renderOnChange(oldState, value = { it.isWordCountEnabled }) { enabled ->
      if (enabled != null) {
        callback.onWordCountChanged(enabled.isEnabled)
      }
    }
  }

  private fun renderLetterCount(
    state: SettingsState,
    oldState: SettingsState?
  ) {
    state.renderOnChange(oldState, value = { it.isLetterCountEnabled }) { enabled ->
      if (enabled != null) {
        callback.onLetterCountChanged(enabled.isEnabled)
      }
    }
  }

  override fun onSaveState(outState: Bundle) {
    toolbarView.saveState(outState)
    settingsView.saveState(outState)
  }

  override fun sync() {
    settingsView.syncState()
  }

}