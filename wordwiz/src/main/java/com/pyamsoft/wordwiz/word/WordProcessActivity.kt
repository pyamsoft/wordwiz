/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.wordwiz.word

import android.content.Intent
import android.os.Bundle
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.helper.Toasty
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.WordProcessPresenter.View
import timber.log.Timber

abstract class WordProcessActivity : ActivityBase(), View {

    internal lateinit var presenter: WordProcessPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        Injector.obtain<WordWizComponent>(applicationContext).inject(this)
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
        Toasty.makeText(applicationContext,
                "An error occurred while attempting to process text", Toasty.LENGTH_SHORT).show()
    }

    override fun onProcessTypeWordCount(count: Int) {
        Toasty.makeText(applicationContext, "Word count: $count", Toasty.LENGTH_SHORT).show()
    }

    override fun onProcessTypeLetterCount(count: Int) {
        Toasty.makeText(applicationContext, "Letter count: $count   ", Toasty.LENGTH_SHORT).show()
    }

    override fun onProcessTypeOccurrences(count: Int, snippet: String) {
        Toasty.makeText(applicationContext, "Occurrence count of snippet '$snippet': $count",
                Toasty.LENGTH_SHORT).show()
    }
}
