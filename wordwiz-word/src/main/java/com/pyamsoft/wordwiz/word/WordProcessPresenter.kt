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

import android.content.ComponentName
import android.os.Bundle
import com.pyamsoft.pydroid.presenter.SchedulerPresenter
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.model.ProcessType
import com.pyamsoft.wordwiz.model.WordProcessResult
import com.pyamsoft.wordwiz.word.WordProcessPresenter.View
import io.reactivex.Scheduler
import timber.log.Timber

class WordProcessPresenter internal constructor(
        private val interactor: WordProcessInteractor,
        computationScheduler: Scheduler, ioScheduler: Scheduler,
        mainThreadScheduler: Scheduler) : SchedulerPresenter<View>(
        computationScheduler, ioScheduler, mainThreadScheduler) {

    fun handleActivityLaunchType(componentName: ComponentName, text: CharSequence,
            extras: Bundle) {
        dispose {
            interactor.getProcessType(componentName, text, extras)
                    .subscribeOn(computationScheduler)
                    .observeOn(mainThreadScheduler)
                    .doAfterTerminate { view?.onProcessComplete() }
                    .doOnSubscribe { view?.onProcessBegin() }
                    .subscribe({ handleProcessType(it) }, {
                        Timber.e(it, "onError handleActivityLaunchType")
                        view?.onProcessError(it)
                    })
        }
    }

    private fun handleProcessType(processType: WordProcessResult) {
        when (processType.type) {
            ProcessType.WORD_COUNT -> view?.onProcessTypeWordCount(processType.count)
            ProcessType.LETTER_COUNT -> view?.onProcessTypeLetterCount(processType.count)
            ProcessType.OCCURRENCES -> {
                val extras: Bundle = processType.extras ?: throw NullPointerException(
                        "Extras is NULL")
                val snippet = extras.getString(WordProcessResult.KEY_EXTRA_SNIPPET,
                        null) ?: throw NullPointerException("Snippet is NULL")
                view?.onProcessTypeOccurrences(processType.count, snippet)
            }
        }
    }

    interface View : WordProcessCallback

    interface WordProcessCallback {

        fun onProcessBegin()

        fun onProcessComplete()

        fun onProcessError(throwable: Throwable)

        fun onProcessTypeWordCount(count: Int)
        fun onProcessTypeLetterCount(count: Int)
        fun onProcessTypeOccurrences(count: Int, snippet: String)
    }
}
