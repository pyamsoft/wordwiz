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

package com.pyamsoft.wordwiz.word

import android.support.annotation.CheckResult
import com.pyamsoft.wordwiz.base.WordWizModule
import io.reactivex.Scheduler

class WordProcessModule(wordWizModule: WordWizModule) {

  private val interactor: WordProcessInteractor
  private val computationScheduler: Scheduler = wordWizModule.provideComputationScheduler()
  private val ioScheduler: Scheduler = wordWizModule.provideIoScheduler()
  private val mainScheduler: Scheduler = wordWizModule.provideMainScheduler()

  init {
    interactor = WordProcessInteractorImpl(wordWizModule.provideContext())
  }

  @CheckResult fun getPresenter(): WordProcessPresenter =
      WordProcessPresenter(interactor, computationScheduler, ioScheduler, mainScheduler)
}
