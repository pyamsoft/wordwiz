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

package com.pyamsoft.wordwiz

import android.app.Application
import android.view.ViewGroup
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.ModuleProvider
import com.pyamsoft.wordwiz.api.WordWizModule
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.pyamsoft.wordwiz.main.MainComponent
import com.pyamsoft.wordwiz.main.MainComponentImpl
import com.pyamsoft.wordwiz.settings.SettingsComponent
import com.pyamsoft.wordwiz.settings.SettingsComponentImpl
import com.pyamsoft.wordwiz.word.WordProcessActivity
import com.pyamsoft.wordwiz.word.WordProcessModule
import com.pyamsoft.wordwiz.word.WordProcessBinder

class WordWizComponentImpl(
  application: Application,
  moduleProvider: ModuleProvider
) : WordWizComponent {

  private val wordProcessModule: WordProcessModule

  init {
    val wordWizModule: WordWizModule = WordWizModuleImpl(
        application, moduleProvider.loaderModule().provideImageLoader()
    )
    wordProcessModule = WordProcessModule(moduleProvider.enforcer(), wordWizModule)
  }

  override fun inject(activity: WordProcessActivity) {
    activity.apply {
      this.binder = WordProcessBinder(wordProcessModule.interactor)
    }
  }

  override fun plusMainComponent(parent: ViewGroup): MainComponent = MainComponentImpl(parent)

  override fun plusSettingsComponent(preferenceScreen: PreferenceScreen): SettingsComponent =
    SettingsComponentImpl(preferenceScreen)

}
