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

package com.pyamsoft.wordwiz.word

import android.content.ComponentName
import androidx.lifecycle.viewModelScope
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.singleJob
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Finish
import com.pyamsoft.wordwiz.word.WordProcessState.Processing
import com.pyamsoft.wordwiz.word.WordProcessViewEvent.CloseScreen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class WordViewModel @Inject internal constructor(
  private val interactor: WordProcessInteractor,
  private val component: ComponentName,
  private val text: CharSequence
) : UiViewModel<WordProcessState, WordProcessViewEvent, WordProcessControllerEvent>(
    initialState = WordProcessState(isProcessing = null, throwable = null, result = null)
) {

  private var processJob by singleJob()

  init {
    process()
  }

  override fun onTeardown() {
    processJob.cancel()
  }

  override fun handleViewEvent(event: WordProcessViewEvent) {
    return when (event) {
      is CloseScreen -> publish(Finish)
    }
  }

  private fun process() {
    processJob = viewModelScope.launch {
      handleProcessBegin()
      try {
        val result = async(context = Dispatchers.Default) {
          interactor.getProcessType(component, text)
        }
        handleProcessSuccess(result.await())
      } catch (e: Throwable) {
        if (e !is CancellationException) {
          Timber.e(e, "Error handling process request")
          handleProcessError(e)
        }
      } finally {
        handleProcessComplete()
      }
    }
  }

  private fun handleProcessBegin() {
    setState { copy(isProcessing = Processing(true)) }
  }

  private fun handleProcessSuccess(result: WordProcessResult) {
    setState { copy(result = result, throwable = null) }
  }

  private fun handleProcessError(throwable: Throwable) {
    setState { copy(result = null, throwable = throwable) }
  }

  private fun handleProcessComplete() {
    setState { copy(isProcessing = Processing(false)) }
  }

}
