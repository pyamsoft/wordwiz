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
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.bus.EventBus
import com.pyamsoft.pydroid.core.singleDisposable
import com.pyamsoft.pydroid.core.tryDispose
import com.pyamsoft.pydroid.arch.BasePresenter
import com.pyamsoft.pydroid.arch.destroy
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.word.WordProcessEvent.Begin
import com.pyamsoft.wordwiz.word.WordProcessEvent.Complete
import com.pyamsoft.wordwiz.word.WordProcessEvent.ProcessError
import com.pyamsoft.wordwiz.word.WordProcessEvent.ProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class WordProcessPresenterImpl internal constructor(
  private val interactor: WordProcessInteractor,
  bus: EventBus<WordProcessEvent>
) : BasePresenter<WordProcessEvent, WordProcessPresenter.Callback>(bus),
    WordProcessPresenter {

  private var processDisposable by singleDisposable()

  override fun onBind() {
    listen()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          return@subscribe when (it) {
            is Begin -> callback.onProcessBegin()
            is ProcessResult -> callback.onProcessSuccess(it.result)
            is ProcessError -> callback.onProcessError(it.error)
            is Complete -> callback.onProcessComplete()
          }
        }
        .destroy(owner)
  }

  override fun onUnbind() {
    processDisposable.tryDispose()
  }

  override fun process(
    component: ComponentName,
    text: CharSequence
  ) {
    processDisposable = interactor.getProcessType(component, text)
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
