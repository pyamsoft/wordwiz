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

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.model.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.model.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.model.WordProcessResult
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase() {

  internal lateinit var viewModel: WordViewModel
  private val handler = Handler(Looper.getMainLooper())

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    super.onCreate(savedInstanceState)
    Injector.obtain<WordWizComponent>(applicationContext)
        .plusWordComponent(this)
        .inject(this)
    observeProcessRequests()
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

  private fun observeProcessRequests() {
    viewModel.onProcessWordCount { wrapper ->
      wrapper.onError { onProcessError() }
      wrapper.onSuccess { onProcessSuccess(it) }
      wrapper.onComplete { onProcessComplete() }
    }
  }

  private fun requestWordProcess() {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    viewModel.handleProcess(componentName, text)
  }

  private fun onProcessComplete() {
    Timber.d("Process complete")

    handler.removeCallbacksAndMessages(null)
    handler.postDelayed({ finish() }, 500)
  }

  private fun onProcessError() {
    Toast.makeText(
        applicationContext,
        "An error occurred while attempting to process text, please try again",
        Toast.LENGTH_SHORT
    )
        .show()
  }

  private fun onProcessSuccess(result: WordProcessResult) {
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
