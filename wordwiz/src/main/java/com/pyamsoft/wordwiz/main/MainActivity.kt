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

package com.pyamsoft.wordwiz.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.util.AppUtil
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.databinding.ActivityMainBinding

class MainActivity : TamperActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_WordWiz_Light)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

    setupToolbarAsActionBar()
    showPreferenceFragment()
  }

  private fun setupToolbarAsActionBar() {
    setSupportActionBar(binding.toolbar)
    binding.toolbar.setTitle(R.string.app_name)
    ViewCompat.setElevation(binding.toolbar, AppUtil.convertToDP(this, 4f))
  }

  private fun showPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    if (fragmentManager.findFragmentByTag(
        MainPreferenceFragment.TAG) == null && fragmentManager.findFragmentByTag(
        AboutLibrariesFragment.TAG) == null) {
      fragmentManager.beginTransaction()
          .add(R.id.main_view_container, MainPreferenceFragment(), MainPreferenceFragment.TAG)
          .commit()
    }
  }

  override fun onBackPressed() {
    val fragmentManager = supportFragmentManager
    val backStackCount = fragmentManager.backStackEntryCount
    if (backStackCount > 0) {
      fragmentManager.popBackStackImmediate()
    } else {
      super.onBackPressed()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val itemId = item.itemId
    val handled: Boolean
    when (itemId) {
      android.R.id.home -> {
        onBackPressed()
        handled = true
      }
      else -> handled = false
    }
    return handled || super.onOptionsItemSelected(item)
  }

  override fun provideApplicationName(): String {
    return "WordWiz"
  }

  override val changeLogLines: Array<String>
    get() {
      val line1 = "BUGFIX: Bugfixes and improvements"
      val line2 = "BUGFIX: Removed all Advertisements"
      val line3 = "BUGFIX: Faster loading of Open Source Licenses page"
      return arrayOf(line1, line2, line3)
    }

  override val versionName: String
    get() = BuildConfig.VERSION_NAME

  override val safePackageName: String
    get() = "com.pyamsoft.wordwiz"

  override val applicationIcon: Int
    get() = R.mipmap.ic_launcher

  override val currentApplicationVersion: Int
    get() = BuildConfig.VERSION_CODE
}
