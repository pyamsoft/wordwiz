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

package com.pyamsoft.wordwiz.word

import android.support.annotation.CheckResult
import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.bus.RxBus
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import com.pyamsoft.wordwiz.api.WordWizModule
import com.pyamsoft.wordwiz.model.WordProcessResult

class WordProcessModule(wordWizModule: WordWizModule) {

  private val interactor: WordProcessInteractor
  private val bus: EventBus<WordProcessResult> = RxBus.create()

  init {
    interactor = WordProcessInteractorImpl(wordWizModule.provideContext())
  }

  @CheckResult
  fun getPresenter(): WordProcessPresenter = WordProcessPresenter(interactor, bus)
}
