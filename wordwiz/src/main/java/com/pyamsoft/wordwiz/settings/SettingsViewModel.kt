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

import com.pyamsoft.pydroid.arch.UiEventHandler
import com.pyamsoft.pydroid.arch.UiState
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.wordwiz.settings.SettingsHandler.SettingsEvent
import com.pyamsoft.wordwiz.settings.SettingsViewModel.SettingsState
import com.pyamsoft.wordwiz.settings.SettingsViewModel.SettingsState.Enabled
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
  private val handler: UiEventHandler<SettingsEvent, SettingsView.Callback>
) : UiViewModel<SettingsState>(
    initialState = SettingsState(isWordCountEnabled = null, isLetterCountEnabled = null)
), SettingsView.Callback {

  override fun onBind() {
    handler.handle(this)
        .destroy()
  }

  override fun onUnbind() {
  }

  override fun onWordCountToggled(enabled: Boolean) {
    setState {
      copy(isWordCountEnabled = Enabled(enabled))
    }
  }

  override fun onLetterCountToggled(enabled: Boolean) {
    setState {
      copy(isLetterCountEnabled = Enabled(enabled))
    }
  }

  data class SettingsState(
    val isWordCountEnabled: Enabled?,
    val isLetterCountEnabled: Enabled?
  ) : UiState {

    data class Enabled(val isEnabled: Boolean)
  }
}