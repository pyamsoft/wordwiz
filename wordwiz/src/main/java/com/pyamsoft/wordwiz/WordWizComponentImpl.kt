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

package com.pyamsoft.wordwiz

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.ui.ModuleProvider
import com.pyamsoft.wordwiz.api.WordWizModule
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.pyamsoft.wordwiz.word.WordComponent
import com.pyamsoft.wordwiz.word.WordComponentImpl
import com.pyamsoft.wordwiz.word.WordProcessModule

class WordWizComponentImpl(
  application: Application,
  moduleProvider: ModuleProvider
) : WordWizComponent {

  private val wordWizModule: WordWizModule = WordWizModuleImpl(
      application, moduleProvider.loaderModule().provideImageLoader()
  )
  private val wordProcessModule = WordProcessModule(moduleProvider.enforcer(), wordWizModule)

  override fun plusWordComponent(owner: LifecycleOwner): WordComponent {
    return WordComponentImpl(owner, wordProcessModule)
  }
}
