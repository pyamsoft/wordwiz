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

package com.pyamsoft.wordwiz.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.preference.PreferenceManager
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.sec.TamperActivity
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.util.toDp
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.databinding.ActivityMainBinding

class MainActivity : TamperActivity() {

  private lateinit var binding: ActivityMainBinding

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    bugfix("Smoother animations")
  }

  override val versionName: String = BuildConfig.VERSION_NAME

  override val safePackageName: String = "com.pyamsoft.wordwiz"

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val currentApplicationVersion: Int = BuildConfig.VERSION_CODE

  override val applicationName: String
    get() = getString(R.string.app_name)

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_WordWiz_Light)
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

    setupToolbarAsActionBar()
    showPreferenceFragment()
  }

  private fun setupToolbarAsActionBar() {
    binding.toolbar.apply {
      setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, 4f.toDp(context).toFloat())

      setNavigationOnClickListener(DebouncedOnClickListener.create {
        onBackPressed()
      })
    }
  }

  private fun showPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    if (fragmentManager.findFragmentByTag(MainFragment.TAG) == null
        && !AboutLibrariesFragment.isPresent(this)
    ) {
      fragmentManager.beginTransaction()
          .add(R.id.main_view_container, MainFragment(), MainFragment.TAG)
          .commit()
    }
  }
}
