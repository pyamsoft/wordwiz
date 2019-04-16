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

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.ui.arch.InvalidIdException
import com.pyamsoft.wordwiz.word.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.word.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.word.WordProcessUiComponent.Callback
import com.pyamsoft.wordwiz.word.WordViewModel.ProcessState
import timber.log.Timber
import javax.inject.Inject

internal class WordProcessUiComponentImpl @Inject internal constructor(
  private val viewModel: WordViewModel
) : BaseUiComponent<Callback>(),
    WordProcessUiComponent {

  override fun id(): Int {
    throw InvalidIdException
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Callback
  ) {
    owner.doOnDestroy {
      viewModel.unbind()
    }

    viewModel.bind { state, oldState ->
      renderLoading(state, oldState)
      renderResult(state, oldState)
      renderError(state, oldState)
    }
  }

  private fun renderLoading(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.renderOnChange(oldState, value = { it.isProcessing }) { processing ->
      if (processing != null) {
        if (processing.isProcessing) {
          callback.beginProcessing()
        } else {
          callback.finishProcessing()
        }
      }
    }
  }

  private fun renderResult(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.renderOnChange(oldState, value = { it.result }) { result ->
      if (result == null) {
        callback.hideMessage()
      } else {
        when (result.type) {
          WORD_COUNT -> callback.showMessage("Word count: ${result.count}")
          LETTER_COUNT -> callback.showMessage("Letter count: ${result.count}")
          else -> Timber.w("Unhandled process success: ${result.type}")
        }
      }
    }
  }

  private fun renderError(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.renderOnChange(oldState, value = { it.throwable }) { throwable ->
      if (throwable == null) {
        callback.hideMessage()
      } else {
        callback.showMessage(throwable.message ?: "Error processing selected text")
      }
    }
  }

  override fun onSaveState(outState: Bundle) {
  }
}

