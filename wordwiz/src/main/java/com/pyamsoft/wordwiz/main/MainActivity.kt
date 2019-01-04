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

package com.pyamsoft.wordwiz.main

import android.os.Bundle
import android.view.View
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent

class MainActivity : RatingActivity() {

  internal lateinit var theming: Theming
  internal lateinit var mainView: MainView

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val rootView: View
    get() = mainView.root()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    Injector.obtain<WordWizComponent>(applicationContext)
        .inject(this)

    if (theming.isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark)
    } else {
      setTheme(R.style.Theme_WordWiz_Light)
    }
    super.onCreate(savedInstanceState)

    mainView.create()

    setupToolbar()
    showPreferenceFragment()
  }

  private fun showPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    if (fragmentManager.findFragmentByTag(MainFragment.TAG) == null
        && !AboutFragment.isPresent(this)
    ) {
      fragmentManager.beginTransaction()
          .add(R.id.main_view_container, MainFragment(), MainFragment.TAG)
          .commit(this)
    }
  }

  private fun setupToolbar() {
    mainView.onToolbarNavClicked { onBackPressed() }
  }
}
