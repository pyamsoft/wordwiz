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
    observeScheduler: Scheduler, subscribeScheduler: Scheduler) : SchedulerPresenter(
    observeScheduler, subscribeScheduler) {

  /**
   * public
   */
  fun handleActivityLaunchType(componentName: ComponentName, text: CharSequence,
      extras: Bundle, callback: ProcessCallback) {
    disposeOnStop(interactor.getProcessType(componentName, text, extras)
        .subscribeOn(subscribeScheduler)
        .observeOn(observeScheduler)
        .doAfterTerminate { callback.onProcessComplete() }
        .doOnSubscribe { callback.onProcessBegin() }
        .subscribe({ wordProcessResult -> handleProcessType(wordProcessResult, callback) }
            , {
          Timber.e(it, "onError handleActivityLaunchType")
          callback.onProcessError()
        }))
  }

  fun handleProcessType(processType: WordProcessResult,
      callback: ProcessCallback) {
    when (processType.type) {
      ProcessType.WORD_COUNT -> callback.onProcessTypeWordCount(processType.count)
      ProcessType.LETTER_COUNT -> callback.onProcessTypeLetterCount(processType.count)
      ProcessType.OCCURRENCES -> {
        val extras: Bundle = processType.extras ?: throw NullPointerException("Extras is NULL")
        val snippet = extras.getString(WordProcessResult.KEY_EXTRA_SNIPPET,
            null) ?: throw NullPointerException("Snippet is NULL")
        callback.onProcessTypeOccurrences(processType.count, snippet)
      }
    }
  }

  interface ProcessCallback {

    fun onProcessBegin()

    fun onProcessError()

    fun onProcessComplete()

    fun onProcessTypeWordCount(wordCount: Int)

    fun onProcessTypeLetterCount(letterCount: Int)

    fun onProcessTypeOccurrences(occurrences: Int, snip: String)
  }
}
