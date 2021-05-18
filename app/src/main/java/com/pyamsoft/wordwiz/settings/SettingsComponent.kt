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

package com.pyamsoft.wordwiz.settings

import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.app.ToolbarActivity
import com.pyamsoft.wordwiz.ViewModelFactoryModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent(modules = [SettingsComponent.ComponentModule::class, ViewModelFactoryModule::class])
internal interface SettingsComponent {

  fun inject(fragment: SettingsPreferenceFragment)

  @Subcomponent.Factory
  interface Factory {

    @CheckResult
    fun create(
        @BindsInstance toolbarActivity: ToolbarActivity,
        @BindsInstance preferenceScreen: PreferenceScreen
    ): SettingsComponent
  }

  @Module
  abstract class ComponentModule {

    @Binds
    @IntoMap
    @ClassKey(SettingsViewModel::class)
    internal abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel
  }
}
