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
import android.widget.Toast
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.helper.Toasty
import com.pyamsoft.wordwiz.Injector
import java.util.Locale
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase() {

  internal lateinit var presenter: WordProcessPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    super.onCreate(savedInstanceState)
    Injector.get().provideComponent().plusWordProcessComponent().inject(this)

    handleIntent(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  override fun onStop() {
    super.onStop()
    presenter.stop()
  }

  override fun onDestroy() {
    super.onDestroy()
    overridePendingTransition(0, 0)
    presenter.destroy()
  }

  private fun handleIntent(intent: Intent) {
    Timber.d("Handle a process text intent")
    val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    presenter.handleActivityLaunchType(componentName, text, getIntent().extras,
        object : WordProcessPresenter.ProcessCallback {

          override fun onProcessBegin() {
            // Start
          }

          override fun onProcessError() {
            Timber.e("An error occurred while attempting to process text")
            Toasty.makeText(applicationContext,
                "An error occurred while attempting to process text", Toasty.LENGTH_SHORT).show()
          }

          override fun onProcessComplete() {
            Timber.d("Process complete")
            finish()
          }

          override fun onProcessTypeWordCount(wordCount: Int) {
            Timber.d("Word count: %d", wordCount)
            Toasty.makeText(applicationContext, "Word count: " + wordCount, Toasty.LENGTH_SHORT)
                .show()
          }

          override fun onProcessTypeLetterCount(letterCount: Int) {
            Timber.d("Letter count: %d", letterCount)
            Toasty.makeText(applicationContext, "Letter count: " + letterCount,
                Toasty.LENGTH_SHORT).show()
          }

          override fun onProcessTypeOccurrences(occurrences: Int, snip: String) {
            Timber.d("Occurrence count of snippet %s: %d", snip, occurrences)
            Toasty.makeText(applicationContext,
                String.format(Locale.getDefault(), "Occurrence count of snippet %s: %d", snip,
                    occurrences), Toasty.LENGTH_SHORT).show()
          }
        })
  }
}
