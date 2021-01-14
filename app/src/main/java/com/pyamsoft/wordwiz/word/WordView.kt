/*
 * Copyright 2020 Peter Kenji Yamanaka
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

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.UiRender
import com.pyamsoft.pydroid.arch.UiView
import com.pyamsoft.pydroid.ui.util.Toaster
import com.pyamsoft.wordwiz.word.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.word.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.word.WordProcessViewEvent.CloseScreen
import timber.log.Timber
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

internal class WordView @Inject internal constructor(
    private val context: Context,
    private val owner: LifecycleOwner
) : UiView<WordProcessState, WordProcessViewEvent>() {

    private val handler by lazy(NONE) { Handler(Looper.getMainLooper()) }

    init {
        doOnTeardown {
            clear()
            hideMessage()
        }
    }

    override fun render(state: UiRender<WordProcessState>) {
        state.mapChanged { it.isProcessing }.render(viewScope) { handleProcessing(it) }
        state.mapChanged { it.result }.render(viewScope) { handleResult(it) }
    }

    private fun handleProcessing(processing: WordProcessState.Processing?) {
        processing?.also { p ->
            clear()
            if (!p.isProcessing) {
                handler.postDelayed({ publish(CloseScreen) }, 750)
            }
        }
    }

    private fun handleResult(result: WordProcessResult?) = if (result == null) hideMessage() else {
        when (result.type) {
            WORD_COUNT -> showMessage("Word count: ${result.count}")
            LETTER_COUNT -> showMessage("Letter count: ${result.count}")
            else -> Timber.d("Unhandled process success: ${result.type} ${result.count}")
        }
    }

    private fun hideMessage() {
        Toaster.bindTo(owner).dismiss()
    }

    fun showError(throwable: Throwable) {
        showMessage(throwable.message ?: "An unexpected error occurred.")
    }

    private fun showMessage(message: String) {
        Toaster.bindTo(owner).short(context.applicationContext, message)
    }

    private fun clear() {
        handler.removeCallbacksAndMessages(null)
    }
}
