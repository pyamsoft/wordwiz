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
import com.pyamsoft.pydroid.arch.UiBinder
import com.pyamsoft.pydroid.core.singleDisposable
import com.pyamsoft.pydroid.core.tryDispose
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.api.WordProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class WordProcessBinder internal constructor(
  private val interactor: WordProcessInteractor
) : UiBinder<WordProcessBinder.Callback>() {

  private var processDisposable by singleDisposable()

  override fun onBind() {
  }

  override fun onUnbind() {
    processDisposable.tryDispose()
  }

  fun process(
    component: ComponentName,
    text: CharSequence
  ) {
    processDisposable = interactor.getProcessType(component, text)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate { callback.onProcessComplete() }
        .doOnSubscribe { callback.onProcessBegin() }
        .subscribe({ callback.onProcessSuccess(it) }, {
          Timber.e(it, "Error handling process request")
          callback.onProcessError(it)
        })
  }

  interface Callback : UiBinder.Callback {

    fun onProcessBegin()

    fun onProcessSuccess(result: WordProcessResult)

    fun onProcessError(throwable: Throwable)

    fun onProcessComplete()

  }

}
