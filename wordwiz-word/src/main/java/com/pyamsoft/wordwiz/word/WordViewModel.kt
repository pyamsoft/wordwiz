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
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.singleDisposable
import com.pyamsoft.pydroid.core.tryDispose
import com.pyamsoft.pydroid.core.viewmodel.BaseViewModel
import com.pyamsoft.pydroid.core.viewmodel.DataBus
import com.pyamsoft.pydroid.core.viewmodel.DataWrapper
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WordViewModel internal constructor(
  owner: LifecycleOwner,
  private val interactor: WordProcessInteractor
) : BaseViewModel(owner) {

  private var handleDisposable by singleDisposable()
  private val processBus = DataBus<WordProcessResult>()

  override fun onCleared() {
    super.onCleared()
    handleDisposable.tryDispose()
  }

  fun onProcessWordCount(func: (DataWrapper<WordProcessResult>) -> Unit) {
    dispose {
      processBus.listen()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(func)
    }
  }

  fun handleProcess(
    componentName: ComponentName,
    text: CharSequence
  ) {
    handleDisposable = interactor.getProcessType(componentName, text)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate { processBus.publishComplete() }
        .doOnSubscribe { processBus.publishLoading(false) }
        .subscribe({ processBus.publishSuccess(it) }, {
          Timber.e(it, "Error handling process request")
          processBus.publishError(it)
        })
  }

}
