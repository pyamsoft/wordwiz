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

import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.threads.Enforcer
import com.pyamsoft.wordwiz.api.WordWizModule
import com.pyamsoft.wordwiz.word.WordComponent
import com.pyamsoft.wordwiz.word.WordComponentImpl
import com.pyamsoft.wordwiz.word.WordProcessModule

class WordWizComponentImpl(
  enforcer: Enforcer,
  module: WordWizModule
) : WordWizComponent {

  private val wordProcessModule = WordProcessModule(enforcer, module)

  override fun plusWordComponent(owner: LifecycleOwner): WordComponent {
    return WordComponentImpl(owner, wordProcessModule)
  }
}
