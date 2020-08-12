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
import androidx.lifecycle.viewModelScope
import com.pyamsoft.highlander.highlander
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.onActualError
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Error
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Finish
import com.pyamsoft.wordwiz.word.WordProcessState.Processing
import com.pyamsoft.wordwiz.word.WordProcessViewEvent.CloseScreen
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

internal class WordViewModel @Inject internal constructor(
    @Named("debug") debug: Boolean,
    interactor: WordProcessInteractor,
    component: ComponentName,
    text: CharSequence
) : UiViewModel<WordProcessState, WordProcessViewEvent, WordProcessControllerEvent>(
    initialState = WordProcessState(isProcessing = null, result = null), debug = debug
) {

    private val processRunner = highlander<Unit> {
        handleProcessBegin()
        try {
            val result = interactor.getProcessType(component, text)
            handleProcessSuccess(result)
        } catch (error: Throwable) {
            error.onActualError { e ->
                Timber.e(e, "Error handling process request")
                handleProcessError(e)
            }
        } finally {
            handleProcessComplete()
        }
    }

    init {
        doOnInit {
            process()
        }
    }

    override fun handleViewEvent(event: WordProcessViewEvent) {
        return when (event) {
            is CloseScreen -> publish(Finish)
        }
    }

    private fun process() {
        viewModelScope.launch(context = Dispatchers.Default) { processRunner.call() }
    }

    private fun handleProcessBegin() {
        setState { copy(isProcessing = Processing(true)) }
    }

    private fun handleProcessSuccess(result: WordProcessResult) {
        setState { copy(result = result) }
    }

    private fun handleProcessError(throwable: Throwable) {
        publish(Error(throwable))
    }

    private fun handleProcessComplete() {
        setState { copy(isProcessing = Processing(false)) }
    }
}
