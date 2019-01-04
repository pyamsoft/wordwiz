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
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WordViewModel internal constructor(private val interactor: WordProcessInteractor) {

  @CheckResult
  fun handleProcess(
    componentName: ComponentName,
    text: CharSequence,
    onProcessBegin: () -> Unit,
    onProcessSuccess: (result: WordProcessResult) -> Unit,
    onProcessError: (error: Throwable) -> Unit,
    onProcessComplete: () -> Unit
  ): Disposable {
    return interactor.getProcessType(componentName, text)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate { onProcessComplete() }
        .doOnSubscribe { onProcessBegin() }
        .subscribe({ onProcessSuccess(it) }, {
          Timber.e(it, "Error handling process request")
          onProcessError(it)
        })
  }

}
