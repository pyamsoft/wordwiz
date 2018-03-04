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
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.base.PYDroidModuleImpl
import com.pyamsoft.pydroid.base.about.Licenses
import com.pyamsoft.pydroid.loader.LoaderModule
import com.pyamsoft.pydroid.loader.LoaderModuleImpl
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class WordWiz : Application() {

  private val component: WordWizComponent by lazy(LazyThreadSafetyMode.NONE) { buildComponent() }
  private val pydroidModule: PYDroidModule by lazy(LazyThreadSafetyMode.NONE) {
    PYDroidModuleImpl(this, BuildConfig.DEBUG)
  }
  private val loaderModule: LoaderModule by lazy(LazyThreadSafetyMode.NONE) {
    LoaderModuleImpl(pydroidModule)
  }
  private lateinit var refWatcher: RefWatcher

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

    PYDroid.init(pydroidModule, loaderModule)
    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase")

  }

  private fun buildComponent(): WordWizComponent = WordWizComponentImpl(
      WordWizModuleImpl(pydroidModule, loaderModule)
  )

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
