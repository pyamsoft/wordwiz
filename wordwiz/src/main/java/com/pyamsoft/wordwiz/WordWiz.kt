/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.wordwiz

import android.app.Application
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.PYDroidModuleImpl
import com.pyamsoft.pydroid.base.about.Licenses
import com.pyamsoft.pydroid.loader.LoaderModule
import com.pyamsoft.pydroid.loader.LoaderModuleImpl
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.wordwiz.base.WordWizModuleImpl
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class WordWiz : Application() {

  private lateinit var refWatcher: RefWatcher
  private var component: WordWizComponent? = null
  private lateinit var pydroidModule: PYDroidModule
  private lateinit var loaderModule: LoaderModule

  override fun onCreate() {
    super.onCreate()
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return
    }

    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase")
    pydroidModule = PYDroidModuleImpl(this, BuildConfig.DEBUG)
    loaderModule = LoaderModuleImpl(pydroidModule)
    PYDroid.init(pydroidModule, loaderModule)

    refWatcher = if (BuildConfig.DEBUG) {
      LeakCanary.install(this)
    } else {
      RefWatcher.DISABLED
    }
  }

  private fun buildComponent(): WordWizComponent = WordWizComponentImpl(
      WordWizModuleImpl(pydroidModule)
  )

  override fun getSystemService(name: String?): Any {
    return if (Injector.name == name) {
      val wordWiz: WordWizComponent
      val obj = component
      if (obj == null) {
        wordWiz = buildComponent()
        component = wordWiz
      } else {
        wordWiz = obj
      }

      // Return
      wordWiz
    } else {
      // Return
      super.getSystemService(name)
    }
  }

  companion object {

    @JvmStatic
    @CheckResult
    fun getRefWatcher(fragment: Fragment): RefWatcher {
      val application = fragment.activity!!.application
      if (application is WordWiz) {
        return application.refWatcher
      } else {
        throw IllegalStateException("Application is not WordWiz")
      }
    }
  }
}
