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

package com.pyamsoft.wordwiz.base

import android.app.Application
import android.content.Context
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.data.Cache
import com.pyamsoft.pydroid.loader.ImageLoader
import com.pyamsoft.pydroid.loader.LoaderModule
import com.pyamsoft.wordwiz.api.WordWizModule
import io.reactivex.Scheduler

class WordWizModuleImpl(
  private val pyDroidModule: PYDroidModule,
  private val loaderModule: LoaderModule
) : WordWizModule {

  override fun provideApplication(): Application = pyDroidModule.provideApplication()

  override fun provideContext(): Context = pyDroidModule.provideContext()

  override fun provideComputationScheduler(): Scheduler =
    pyDroidModule.provideComputationScheduler()

  override fun provideIoScheduler(): Scheduler = pyDroidModule.provideIoScheduler()

  override fun provideMainThreadScheduler(): Scheduler = pyDroidModule.provideMainThreadScheduler()

  override fun provideImageLoader(): ImageLoader = loaderModule.provideImageLoader()

  override fun provideImageLoaderCache(): Cache = loaderModule.provideImageLoaderCache()

}
