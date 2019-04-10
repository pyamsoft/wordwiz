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
import android.os.Handler
import android.os.Looper
import com.pyamsoft.pydroid.ui.app.ActivityBase
import com.pyamsoft.pydroid.ui.theme.ThemeInjector
import com.pyamsoft.pydroid.ui.util.Toaster
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.api.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.api.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.word.WordProcessPresenter.ProcessState
import timber.log.Timber
import kotlin.LazyThreadSafetyMode.NONE

internal abstract class WordProcessActivity : ActivityBase(), WordProcessPresenter.Callback {

  private val handler by lazy(NONE) { Handler(Looper.getMainLooper()) }

  internal lateinit var presenter: WordProcessPresenter

  final override val fragmentContainerId: Int = 0

  final override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    if (ThemeInjector.obtain(applicationContext).isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark_Transparent)
    } else {
      setTheme(R.style.Theme_WordWiz_Light_Transparent)
    }
    super.onCreate(savedInstanceState)

    Injector.obtain<WordWizComponent>(applicationContext)
        .plusWordComponent()
        .inject(this)
    presenter.bind(this)
  }

  final override fun onStop() {
    super.onStop()
    if (!isFinishing && !isChangingConfigurations) {
      finish()
    }
  }

  final override fun finish() {
    super.finish()
    overridePendingTransition(0, 0)
  }

  final override fun onDestroy() {
    super.onDestroy()
    overridePendingTransition(0, 0)
    handler.removeCallbacksAndMessages(null)
    presenter.unbind()
  }

  final override fun onRender(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    renderLoading(state, oldState)
    renderResult(state, oldState)
    renderError(state, oldState)
  }

  private fun renderLoading(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.isProcessing.let { processing ->
      if (oldState == null || oldState.isProcessing != processing) {
        if (processing) {
          handler.removeCallbacksAndMessages(null)
        } else {
          handler.removeCallbacksAndMessages(null)
          handler.postDelayed({ finish() }, 750)
        }
      }
    }
  }

  private fun renderResult(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.result.let { result ->
      if (oldState == null || oldState.result != result) {
        if (result == null) {
          dismissToast()
        } else {
          when (result.type) {
            WORD_COUNT -> toast("Word count: ${result.count}")
            LETTER_COUNT -> toast("Letter count: ${result.count}")
            else -> Timber.w("Unhandled process success: ${result.type}")
          }
        }
      }
    }
  }

  private fun renderError(
    state: ProcessState,
    oldState: ProcessState?
  ) {
    state.throwable.let { throwable ->
      if (oldState == null || oldState.throwable != throwable) {
        if (throwable == null) {
          dismissToast()
        } else {
          toast(throwable.message ?: "Error processing selected text")
        }
      }
    }
  }

  private fun dismissToast() {
    Toaster.bindTo(this)
        .dismiss()
  }

  private fun toast(message: String) {
    Toaster.bindTo(this)
        .short(applicationContext, message)
        .show()
  }
}
