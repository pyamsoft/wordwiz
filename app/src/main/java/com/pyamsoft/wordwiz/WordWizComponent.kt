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

import android.content.Context
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.core.threads.Enforcer
import com.pyamsoft.wordwiz.main.MainComponent
import com.pyamsoft.wordwiz.settings.SettingsComponent
import com.pyamsoft.wordwiz.word.WordComponent
import com.pyamsoft.wordwiz.word.WordProcessModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WordProcessModule::class, ViewModelModule::class])
internal interface WordWizComponent {

  @CheckResult
  fun plusWordComponent(): WordComponent.Factory

  @CheckResult
  fun plusSettingsComponent(): SettingsComponent.Factory

  @CheckResult
  fun plusMainComponent(): MainComponent.Factory

  @Component.Factory
  interface Factory {

    @CheckResult
    fun create(
      @BindsInstance context: Context,
      @BindsInstance enforcer: Enforcer
    ): WordWizComponent
  }

}
