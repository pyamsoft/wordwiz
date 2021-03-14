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

package com.pyamsoft.wordwiz.word

import android.content.ComponentName
import com.pyamsoft.highlander.highlander
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.UnitControllerEvent
import com.pyamsoft.pydroid.arch.onActualError
import com.pyamsoft.wordwiz.word.WordProcessState.Processing
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

internal class WordViewModel @Inject internal constructor(
    interactor: WordProcessInteractor,
    component: ComponentName,
    text: CharSequence
) : UiViewModel<WordProcessState, WordProcessViewEvent, UnitControllerEvent>(
    WordProcessState(
        isProcessing = null,
        result = null,
        error = null
    )
) {

    private val processRunner = highlander<Unit> {
        try {
            val result = interactor.getProcessType(component, text)
            handleProcessSuccess(result)
        } catch (error: Throwable) {
            error.onActualError { e ->
                Timber.e(e, "Error handling process request")
                handleProcessError(e)
            }
        }
    }

    internal fun handleProcess(scope: CoroutineScope) {
        scope.setState(stateChange = { copy(isProcessing = Processing(true)) }, andThen = {
            processRunner.call()
            setState { copy(isProcessing = Processing(false)) }
        })
    }

    private fun handleProcessError(throwable: Throwable) {
        setState { copy(error = throwable) }
    }

    private fun handleProcessSuccess(result: WordProcessResult) {
        setState { copy(result = result) }
    }
}
