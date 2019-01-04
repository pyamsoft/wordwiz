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
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.ModuleProvider
import com.pyamsoft.wordwiz.api.WordWizModule
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.pyamsoft.wordwiz.main.MainActivity
import com.pyamsoft.wordwiz.main.MainPrefComponent
import com.pyamsoft.wordwiz.main.MainPrefComponentImpl
import com.pyamsoft.wordwiz.main.MainViewImpl
import com.pyamsoft.wordwiz.word.WordProcessActivity
import com.pyamsoft.wordwiz.word.WordProcessModule

class WordWizComponentImpl(
  application: Application,
  moduleProvider: ModuleProvider
) : WordWizComponent {

  private val theming = moduleProvider.theming()
  private val wordWizModule: WordWizModule = WordWizModuleImpl(
      application, moduleProvider.loaderModule().provideImageLoader()
  )
  private val wordProcessModule = WordProcessModule(moduleProvider.enforcer(), wordWizModule)

  override fun inject(activity: MainActivity) {
    activity.theming = theming
    activity.mainView = MainViewImpl(activity)
  }

  override fun inject(activity: WordProcessActivity) {
    activity.theming = theming
    activity.viewModel = wordProcessModule.getViewModel()
  }

  override fun plusMainPrefComponent(
    owner: LifecycleOwner,
    preferenceScreen: PreferenceScreen
  ): MainPrefComponent {
    return MainPrefComponentImpl(theming, owner, preferenceScreen)
  }
}
