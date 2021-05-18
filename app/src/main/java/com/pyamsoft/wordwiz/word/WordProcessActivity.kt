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

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.pyamsoft.pydroid.arch.*
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.ActivityBase
import com.pyamsoft.pydroid.ui.arch.fromViewModelFactory
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.WordWizViewModelFactory
import javax.inject.Inject

internal abstract class WordProcessActivity : ActivityBase(), UiController<UnitControllerEvent> {

  @JvmField @Inject internal var factory: WordWizViewModelFactory? = null
  private val viewModel by fromViewModelFactory<WordViewModel> { factory?.create(this) }

  @JvmField @Inject internal var view: WordView? = null

  private var stateSaver: StateSaver? = null

  final override val fragmentContainerId: Int = 0

  final override val applicationIcon: Int = 0

  final override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    setTheme(R.style.Theme_WordWiz_Transparent)
    super.onCreate(savedInstanceState)

    val text: CharSequence? = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    if (text == null || text.isBlank()) {
      finish()
      return
    }

    Injector.obtainFromApplication<WordWizComponent>(this)
        .plusWordComponent()
        .create(this, componentName, text)
        .inject(this)

    stateSaver =
        createComponent(savedInstanceState, this, viewModel, this, requireNotNull(view)) {
          return@createComponent when (it) {
            is WordProcessViewEvent.CloseScreen -> finish()
          }
        }

    viewModel.handleProcess(lifecycleScope)
  }

  final override fun onStop() {
    super.onStop()
    if (!isFinishing && !isChangingConfigurations) {
      finish()
    }
  }

  final override fun onControllerEvent(event: UnitControllerEvent) {}

  final override fun onSaveInstanceState(outState: Bundle) {
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
