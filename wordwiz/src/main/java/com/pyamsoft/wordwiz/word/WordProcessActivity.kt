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

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.pyamsoft.pydroid.ui.app.ActivityBase
import com.pyamsoft.pydroid.ui.theme.ThemeInjector
import com.pyamsoft.pydroid.ui.util.Toaster
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.api.WordProcessResult
import com.pyamsoft.wordwiz.api.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.api.ProcessType.WORD_COUNT
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase(), WordProcessPresenter.Callback {

  internal lateinit var presenter: WordProcessPresenter

  private val handler = Handler(Looper.getMainLooper())

  override val fragmentContainerId: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    if (ThemeInjector.obtain(applicationContext).isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark_Transparent)
    } else {
      setTheme(R.style.Theme_WordWiz_Light_Transparent)
    }

    Injector.obtain<WordWizComponent>(applicationContext)
        .inject(this)

    super.onCreate(savedInstanceState)
    presenter.bind(this, this)
    requestWordProcess()
  }

  override fun onStop() {
    super.onStop()
    if (!isFinishing && !isChangingConfigurations) {
      finish()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    overridePendingTransition(0, 0)
    handler.removeCallbacksAndMessages(null)
  }

  private fun requestWordProcess() {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    presenter.process(componentName, text)
  }

  override fun onProcessBegin() {
    Timber.d("Process begin")
    handler.removeCallbacksAndMessages(null)
  }

  override fun onProcessComplete() {
    Timber.d("Process complete")

    handler.removeCallbacksAndMessages(null)
    handler.postDelayed({ finish() }, 500)
  }

  override fun onProcessError(throwable: Throwable) {
    Toaster.bindTo(this)
        .short(applicationContext, throwable.message ?: "Error processing selected text")
        .show()
  }

  override fun onProcessSuccess(result: WordProcessResult) {
    when (result.type) {
      WORD_COUNT -> onProcessTypeWordCount(result.count)
      LETTER_COUNT -> onProcessTypeLetterCount(result.count)
      else -> Timber.w("Unhandled process success: ${result.type}")
    }
  }

  private fun onProcessTypeWordCount(count: Int) {
    Toaster.bindTo(this)
        .short(applicationContext, "Word count: $count")
        .show()
  }

  private fun onProcessTypeLetterCount(count: Int) {
    Toaster.bindTo(this)
        .short(applicationContext, "Letter count: $count   ")
        .show()
  }
}
