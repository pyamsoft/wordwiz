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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowUiComponent
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWiz
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.settings.MainFragment

class MainActivity : RatingActivity() {

  internal lateinit var toolbarComponent: MainToolbarUiComponent
  internal lateinit var frameComponent: MainFrameUiComponent
  internal lateinit var dropshadowComponent: DropshadowUiComponent

  private lateinit var layoutRoot: ConstraintLayout

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val snackbarRoot: View
    get() = layoutRoot

  override val fragmentContainerId: Int
    get() = frameComponent.id()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (WordWiz.theming(this).isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark)
    } else {
      setTheme(R.style.Theme_WordWiz_Light)
    }
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_constraint)
    layoutRoot = findViewById(R.id.layout_constraint)

    Injector.obtain<WordWizComponent>(applicationContext)
        .plusMainComponent(layoutRoot, this)
        .inject(this)

    createComponents(savedInstanceState)
    layoutComponents(layoutRoot)
    showPreferenceFragment()
  }

  private fun createComponents(savedInstanceState: Bundle?) {
    toolbarComponent.create(savedInstanceState)
    frameComponent.create(savedInstanceState)
    dropshadowComponent.create(savedInstanceState)
  }

  private fun layoutComponents(layoutRoot: ConstraintLayout) {
    ConstraintSet().apply {
      clone(layoutRoot)

      toolbarComponent.also {
        connect(it.id(), ConstraintSet.TOP, layoutRoot.id, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      frameComponent.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.BOTTOM, layoutRoot.id, ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      dropshadowComponent.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, layoutRoot.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, layoutRoot.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      applyTo(layoutRoot)
    }
  }

  private fun showPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    if (fragmentManager.findFragmentByTag(MainFragment.TAG) == null
        && !AboutFragment.isPresent(this)
    ) {
      fragmentManager.beginTransaction()
          .add(
              fragmentContainerId,
              MainFragment(), MainFragment.TAG
          )
          .commit(this)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbarComponent.saveState(outState)
    frameComponent.saveState(outState)
    dropshadowComponent.saveState(outState)
  }
}
