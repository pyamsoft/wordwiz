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

package com.pyamsoft.wordwiz

import android.support.annotation.CheckResult
import com.pyamsoft.wordwiz.base.WordWizModule
import com.pyamsoft.wordwiz.word.WordProcessComponent
import com.pyamsoft.wordwiz.word.WordProcessModule

class WordWizComponent private constructor(module: WordWizModule) {

  private val wordProcessComponent: WordProcessComponent = WordProcessComponent(
      WordProcessModule(module))

  @CheckResult fun plusWordProcessComponent(): WordProcessComponent {
    return wordProcessComponent
  }

  companion object {

    @JvmStatic
    @CheckResult internal fun withModule(module: WordWizModule): WordWizComponent {
      return WordWizComponent(module)
    }
  }
}
