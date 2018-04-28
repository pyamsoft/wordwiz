/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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
import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.presenter.Presenter
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.model.ProcessType
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WordProcessPresenter internal constructor(
  private val interactor: WordProcessInteractor,
  private val bus: EventBus<WordProcessResult>
) : Presenter<WordProcessPresenter.View>() {

  override fun onCreate() {
    super.onCreate()
    dispose {
      bus.listen()
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe { handleProcessType(it) }
    }
  }

  fun handleActivityLaunchType(
    componentName: ComponentName,
    text: CharSequence,
    extras: Bundle
  ) {
    dispose {
      interactor.getProcessType(componentName, text, extras)
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .doAfterTerminate { view?.onProcessComplete() }
          .doOnSubscribe { view?.onProcessBegin() }
          .subscribe({ bus.publish(it) }, {
            Timber.e(it, "onError handleActivityLaunchType")
            view?.onProcessError(it)
          })
    }
  }

  private fun handleProcessType(processType: WordProcessResult) {
    when (processType.type) {
      ProcessType.WORD_COUNT -> view?.onProcessTypeWordCount(processType.count)
      ProcessType.LETTER_COUNT -> view?.onProcessTypeLetterCount(processType.count)
      ProcessType.OCCURRENCES -> {
        val extras: Bundle = processType.extras ?: throw NullPointerException(
            "Extras is NULL"
        )
        val snippet = extras.getString(
            WordProcessResult.KEY_EXTRA_SNIPPET,
            null
        ) ?: throw NullPointerException("Snippet is NULL")
        view?.onProcessTypeOccurrences(processType.count, snippet)
      }
    }
  }

  interface View : WordProcessCallback

  interface WordProcessCallback {

    fun onProcessBegin()

    fun onProcessComplete()

    fun onProcessError(throwable: Throwable)

    fun onProcessTypeWordCount(count: Int)
    fun onProcessTypeLetterCount(count: Int)
    fun onProcessTypeOccurrences(
      count: Int,
      snippet: String
    )
  }

}
