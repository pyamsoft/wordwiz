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

import android.app.Application
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.pydroid.ui.PYDroid.Instance
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class WordWiz : Application(), Instance {

  private var pyDroid: PYDroid? = null
  private lateinit var refWatcher: RefWatcher
  private lateinit var component: WordWizComponent

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    if (BuildConfig.DEBUG) {
      refWatcher = LeakCanary.install(this)
    } else {
      refWatcher = RefWatcher.DISABLED
    }

    PYDroid.init(this, this, BuildConfig.DEBUG)
  }

  override fun getPydroid(): PYDroid? = pyDroid

  override fun setPydroid(instance: PYDroid) {
    pyDroid = instance.also {
      val loaderModule = it.modules()
          .loaderModule()
      component = WordWizComponentImpl(
          it.enforcer(),
          WordWizModuleImpl(
              this,
              loaderModule.provideImageLoader(),
              loaderModule.provideImageLoaderCache()
          )
      )
    }
  }

  override fun getSystemService(name: String?): Any {
    if (Injector.name == name) {
      return component
    } else {
      return super.getSystemService(name)
    }
  }

  companion object {

    @JvmStatic
    @CheckResult
    fun getRefWatcher(fragment: Fragment): RefWatcher {
      val application = fragment.requireActivity()
          .application
      if (application is WordWiz) {
        return application.refWatcher
      } else {
        throw IllegalStateException("Application is not WordWiz")
      }
    }
  }
}
