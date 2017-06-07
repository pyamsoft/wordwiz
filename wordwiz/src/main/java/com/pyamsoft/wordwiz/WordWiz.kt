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

package com.pyamsoft.wordwiz

import android.app.Application
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import com.pyamsoft.pydroid.about.Licenses
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.wordwiz.base.WordWizModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class WordWiz : Application() {

  private var refWatcher: RefWatcher? = null

  private var component: WordWizComponent? = null

  @CheckResult fun getComponent(): WordWizComponent {
    val obj = component
    if (obj == null) {
      throw IllegalStateException("WordWizComponent must be initialized before use")
    } else {
      return obj
    }
  }

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase")
    PYDroid.initialize(this, BuildConfig.DEBUG)

    component = WordWizComponent.withModule(WordWizModule(applicationContext))

    if (BuildConfig.DEBUG) {
      refWatcher = LeakCanary.install(this)
    } else {
      refWatcher = RefWatcher.DISABLED
    }
  }

  companion object {

    @JvmStatic
    @CheckResult fun getRefWatcher(fragment: Fragment): RefWatcher {
      val application = fragment.activity.application
      if (application is WordWiz) {
        return application.refWatcher!!
      } else {
        throw IllegalStateException("Application is not WordWiz")
      }
    }
  }
}
