/*
 * Copyright 2017 Peter Kenji Yamanaka
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
 */

package com.pyamsoft.wordwiz.word

import android.content.Intent
import android.os.Bundle
import com.pyamsoft.pydroid.presenter.Presenter
import com.pyamsoft.pydroid.ui.app.activity.DisposableActivity
import com.pyamsoft.pydroid.ui.helper.Toasty
import com.pyamsoft.wordwiz.Injector
import timber.log.Timber

abstract class WordProcessActivity : DisposableActivity() {

  internal lateinit var presenter: WordProcessPresenter

  override fun provideBoundPresenters(): List<Presenter<*>> = listOf(presenter)

  override val shouldConfirmBackPress: Boolean
    get() = false

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    super.onCreate(savedInstanceState)

    Injector.with(this) {
      it.inject(this)
    }

    presenter.bind(Unit)

    handleIntent(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  override fun onDestroy() {
    super.onDestroy()
    overridePendingTransition(0, 0)
  }

  private fun handleIntent(intent: Intent) {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    presenter.handleActivityLaunchType(componentName, text, getIntent().extras, onProcessBegin = {
      Timber.d("Start processing")
    }
        , onProcessError = {
      Timber.e(it, "An error occurred while attempting to process text")
      Toasty.makeText(applicationContext,
          "An error occurred while attempting to process text", Toasty.LENGTH_SHORT).show()
    }, onProcessTypeWordCount = {
      Toasty.makeText(applicationContext, "Word count: $it", Toasty.LENGTH_SHORT)
          .show()
    }, onProcessTypeLetterCount = {
      Toasty.makeText(applicationContext, "Letter count: $it",
          Toasty.LENGTH_SHORT).show()

    }, onProcessTypeOccurrences = { occurrences, snippet ->
      Toasty.makeText(applicationContext, "Occurrence count of snippet '$snippet': $occurrences",
          Toasty.LENGTH_SHORT).show()
    }, onProcessComplete = {
      Timber.d("Process complete")
      finish()
    })
  }
}
