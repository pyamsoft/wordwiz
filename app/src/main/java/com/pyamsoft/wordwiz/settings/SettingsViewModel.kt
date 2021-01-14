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

import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.wordwiz.settings.SettingsControllerEvent.LetterCountAction
import com.pyamsoft.wordwiz.settings.SettingsControllerEvent.WordCountAction
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleLetterCount
import com.pyamsoft.wordwiz.settings.SettingsViewEvent.ToggleWordCount
import com.pyamsoft.wordwiz.word.ComponentManager
import javax.inject.Inject

internal class SettingsViewModel @Inject internal constructor(
    manager: ComponentManager
) : UiViewModel<SettingsViewState, SettingsViewEvent, SettingsControllerEvent>(
    SettingsViewState(
        isWordCountEnabled = manager.isWordCountEnabled(),
        isLetterCountEnabled = manager.isLetterCountEnabled()
    )
) {

    private fun onWordCountToggled() {
        setState(
            stateChange = { copy(isWordCountEnabled = !isWordCountEnabled) },
            andThen = { newState ->
                publish(WordCountAction(newState.isWordCountEnabled))
            })
    }

    private fun onLetterCountToggled() {
        setState(
            stateChange = { copy(isLetterCountEnabled = !isLetterCountEnabled) },
            andThen = { newState ->
                publish(LetterCountAction(newState.isLetterCountEnabled))
            })
    }

    override fun handleViewEvent(event: SettingsViewEvent) = when (event) {
        is ToggleWordCount -> onWordCountToggled()
        is ToggleLetterCount -> onLetterCountToggled()
    }
}
