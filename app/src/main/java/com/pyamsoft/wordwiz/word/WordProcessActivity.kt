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
import androidx.lifecycle.ViewModelProviders
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.ActivityBase
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.WordProcessControllerEvent.Finish
import javax.inject.Inject

internal abstract class WordProcessActivity : ActivityBase() {

  @JvmField @Inject internal var factory: ViewModelProvider.Factory? = null
  @JvmField @Inject internal var view: WordView? = null
  private var viewModel: WordViewModel? = null

  final override val fragmentContainerId: Int = 0

  final override fun onCreate(savedInstanceState: Bundle?) {
    overridePendingTransition(0, 0)
    if (Injector.obtain<Theming>(applicationContext).isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark_Transparent)
    } else {
      setTheme(R.style.Theme_WordWiz_Light_Transparent)
    }
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

    ViewModelProviders.of(this, factory)
        .let { factory ->
          viewModel = factory.get(WordViewModel::class.java)
        }

    createComponent(
        savedInstanceState, this,
        requireNotNull(viewModel),
        requireNotNull(view)
    ) {
      return@createComponent when (it) {
        is Finish -> finish()
      }
    }
  }

  final override fun onStop() {
    super.onStop()
    if (!isFinishing && !isChangingConfigurations) {
      finish()
    }
  }

  final override fun finish() {
    super.finish()
    overridePendingTransition(0, 0)
  }

  final override fun onDestroy() {
    super.onDestroy()
    overridePendingTransition(0, 0)

    factory = null
    view = null
    viewModel = null
  }
}
