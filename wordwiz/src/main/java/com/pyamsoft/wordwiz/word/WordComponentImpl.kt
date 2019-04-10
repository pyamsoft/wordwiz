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
import com.pyamsoft.wordwiz.api.WordProcessInteractor

internal class WordComponentImpl internal constructor(
  private val interactor: WordProcessInteractor
) : WordComponent {

  override fun inject(activity: WordProcessActivity) {
    val text = activity.intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
    val componentName = activity.componentName

    activity.apply {
      this.presenter = WordProcessPresenter(interactor, componentName, text)
    }
  }

}
