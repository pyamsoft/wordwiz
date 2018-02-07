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
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.util.Toasty
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.WordProcessPresenter.View
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase(), View {

  internal lateinit var presenter: WordProcessPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    super.onCreate(savedInstanceState)
    Injector.obtain<WordWizComponent>(applicationContext)
        .inject(this)
    presenter.bind(this, this)
    handleIntent(intent)
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
  }

  private fun handleIntent(intent: Intent) {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    presenter.handleActivityLaunchType(componentName, text, getIntent().extras)
  }

  override fun onProcessBegin() {
    Timber.d("Start processing")
  }

  override fun onProcessComplete() {
    Timber.d("Process complete")
    finish()
  }

  override fun onProcessError(throwable: Throwable) {
    Timber.e(throwable, "An error occurred while attempting to process text")
    Toasty.makeText(
        applicationContext,
        "An error occurred while attempting to process text", Toasty.LENGTH_SHORT
    )
        .show()
  }

  override fun onProcessTypeWordCount(count: Int) {
    Toasty.makeText(applicationContext, "Word count: $count", Toasty.LENGTH_SHORT)
        .show()
  }

  override fun onProcessTypeLetterCount(count: Int) {
    Toasty.makeText(applicationContext, "Letter count: $count   ", Toasty.LENGTH_SHORT)
        .show()
  }

  override fun onProcessTypeOccurrences(
    count: Int,
    snippet: String
  ) {
    Toasty.makeText(
        applicationContext, "Occurrence count of snippet '$snippet': $count",
        Toasty.LENGTH_SHORT
    )
        .show()
  }
}
