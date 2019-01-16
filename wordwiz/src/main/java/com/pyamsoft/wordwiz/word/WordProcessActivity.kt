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
import android.widget.Toast
import com.pyamsoft.pydroid.core.singleDisposable
import com.pyamsoft.pydroid.core.tryDispose
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.arch.destroy
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.model.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.model.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.model.WordProcessResult
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.Begin
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.Complete
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.ProcessError
import com.pyamsoft.wordwiz.word.WordProcessStateEvent.ProcessResult
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase() {

  internal lateinit var theming: Theming
  internal lateinit var worker: WordProcessWorker

  private val handler = Handler(Looper.getMainLooper())

  private var processDisposable by singleDisposable()

  override val fragmentContainerId: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)

    Injector.obtain<WordWizComponent>(applicationContext)
        .inject(this)

    if (theming.isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark_Transparent)
    } else {
      setTheme(R.style.Theme_WordWiz_Light_Transparent)
    }
    super.onCreate(savedInstanceState)

    worker.onProcessEvent {
      return@onProcessEvent when (it) {
        Begin -> onProcessBegin()
        is ProcessResult -> onProcessResult(it.result)
        is ProcessError -> onProcessError(it.error)
        Complete -> onProcessComplete()
      }
    }
        .destroy(this)

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
    processDisposable.tryDispose()
  }

  private fun requestWordProcess() {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    processDisposable = worker.handleProcess(componentName, text)
  }

  private fun onProcessBegin() {
    Timber.d("Process begin")
    handler.removeCallbacksAndMessages(null)
  }

  private fun onProcessComplete() {
    Timber.d("Process complete")

    handler.removeCallbacksAndMessages(null)
    handler.postDelayed({ finish() }, 500)
  }

  private fun onProcessError(error: Throwable) {
    Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT)
        .show()
  }

  private fun onProcessResult(result: WordProcessResult) {
    when (result.type) {
      WORD_COUNT -> onProcessTypeWordCount(result.count)
      LETTER_COUNT -> onProcessTypeLetterCount(result.count)
      else -> Timber.w("Unhandled process success: ${result.type}")
    }
  }

  private fun onProcessTypeWordCount(count: Int) {
    Toast.makeText(applicationContext, "Word count: $count", Toast.LENGTH_SHORT)
        .show()
  }

  private fun onProcessTypeLetterCount(count: Int) {
    Toast.makeText(applicationContext, "Letter count: $count   ", Toast.LENGTH_SHORT)
        .show()
  }
}
