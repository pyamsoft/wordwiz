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
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.ActivityBase
import com.pyamsoft.pydroid.ui.arch.factory
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Error
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Finish
import javax.inject.Inject

internal abstract class WordProcessActivity : ActivityBase() {

    @JvmField
    @Inject
    internal var factory: ViewModelProvider.Factory? = null
    private val viewModel by factory<WordViewModel> { factory }

    @JvmField
    @Inject
    internal var view: WordView? = null

    private var stateSaver: StateSaver? = null

    final override val fragmentContainerId: Int = 0

    final override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        setTheme(R.style.Theme_WordWiz_Transparent)
        super.onCreate(savedInstanceState)

        val text: CharSequence? = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        if (text == null || text.isBlank()) {
            finish()
            return
        }

        Injector.obtain<WordWizComponent>(applicationContext)
            .plusWordComponent()
            .create(this, componentName, text)
            .inject(this)

        stateSaver = createComponent(
            savedInstanceState, this,
            viewModel,
            requireNotNull(view)
        ) {
            return@createComponent when (it) {
                is Finish -> finish()
                is Error -> requireNotNull(view).showError(it.throwable)
            }
        }
    }

    final override fun onStop() {
        super.onStop()
        if (!isFinishing && !isChangingConfigurations) {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stateSaver?.saveState(outState)
    }

    final override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    final override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(0, 0)

        stateSaver = null
        factory = null
        view = null
    }
}
