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
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.core.bus.EventBus
import com.pyamsoft.pydroid.ui.arch.Worker
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.Begin
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.Complete
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.ProcessError
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.ProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class WordProcessWorker internal constructor(
  private val interactor: WordProcessInteractor,
  bus: EventBus<WordProcessStateEvent>
) : Worker<WordProcessStateEvent>(bus) {

  @CheckResult
  fun onProcessEvent(func: (event: WordProcessStateEvent) -> Unit): Disposable {
    return listen()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(func)
  }

  @CheckResult
  fun handleProcess(
    componentName: ComponentName,
    text: CharSequence
  ): Disposable {
    return interactor.getProcessType(componentName, text)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate { publish(Complete) }
        .doOnSubscribe { publish(Begin) }
        .subscribe({ publish(ProcessResult(it)) }, {
          Timber.e(it, "Error handling process request")
          publish(ProcessError(it))
        })
  }

}
