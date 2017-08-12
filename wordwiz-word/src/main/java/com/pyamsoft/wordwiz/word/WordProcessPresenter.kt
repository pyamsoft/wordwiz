/*
 * Copyright 2017 Peter Kenji Yamanaka
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
import android.os.Bundle
import com.pyamsoft.pydroid.presenter.SchedulerPresenter
import com.pyamsoft.wordwiz.model.ProcessType
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.Scheduler
import timber.log.Timber

class WordProcessPresenter internal constructor(
    private val interactor: WordProcessInteractor,
    computationScheduler: Scheduler, ioScheduler: Scheduler,
    mainThreadScheduler: Scheduler) : SchedulerPresenter<Unit>(
    computationScheduler, ioScheduler, mainThreadScheduler) {

  /**
   * public
   */
  fun handleActivityLaunchType(componentName: ComponentName, text: CharSequence,
      extras: Bundle, onProcessBegin: () -> Unit, onProcessError: (Throwable) -> Unit,
      onProcessTypeWordCount: (Int) -> Unit, onProcessTypeLetterCount: (Int) -> Unit,
      onProcessTypeOccurrences: (Int, String) -> Unit, onProcessComplete: () -> Unit) {
    disposeOnStop {
      interactor.getProcessType(componentName, text, extras)
          .subscribeOn(computationScheduler)
          .observeOn(mainThreadScheduler)
          .doAfterTerminate { onProcessComplete() }
          .doOnSubscribe { onProcessBegin() }
          .subscribe({
            handleProcessType(it, onProcessTypeWordCount = onProcessTypeWordCount,
                onProcessTypeLetterCount = onProcessTypeLetterCount,
                onProcessTypeOccurrences = onProcessTypeOccurrences)
          }, {
            Timber.e(it, "onError handleActivityLaunchType")
            onProcessError(it)
          })
    }
  }

  private fun handleProcessType(processType: WordProcessResult,
      onProcessTypeWordCount: (Int) -> Unit,
      onProcessTypeLetterCount: (Int) -> Unit, onProcessTypeOccurrences: (Int, String) -> Unit) {
    when (processType.type) {
      ProcessType.WORD_COUNT -> onProcessTypeWordCount(processType.count)
      ProcessType.LETTER_COUNT -> onProcessTypeLetterCount(processType.count)
      ProcessType.OCCURRENCES -> {
        val extras: Bundle = processType.extras ?: throw NullPointerException("Extras is NULL")
        val snippet = extras.getString(WordProcessResult.KEY_EXTRA_SNIPPET,
            null) ?: throw NullPointerException("Snippet is NULL")
        onProcessTypeOccurrences(processType.count, snippet)
      }
    }
  }
}
