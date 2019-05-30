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
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.about.AboutFragment
import com.pyamsoft.pydroid.ui.rating.ChangeLogBuilder
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.buildChangeLog
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.util.layout
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.settings.SettingsFragment
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : RatingActivity() {

  @JvmField @Inject internal var toolbar: MainToolbarView? = null
  @JvmField @Inject internal var view: MainFrameView? = null
  @JvmField @Inject internal var dropshadowView: DropshadowView? = null

  override val versionName: String = BuildConfig.VERSION_NAME

  override val applicationIcon: Int = R.mipmap.ic_launcher

  override val snackbarRoot: ViewGroup by lazy(NONE) {
    findViewById<CoordinatorLayout>(R.id.snackbar_root)
  }

  override val fragmentContainerId: Int
    get() = requireNotNull(view).id()

  override val changeLogLines: ChangeLogBuilder = buildChangeLog {
    change("New icon style")
    change("Better open source license viewing experience")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    if (Injector.obtain<Theming>(applicationContext).isDarkTheme()) {
      setTheme(R.style.Theme_WordWiz_Dark)
    } else {
      setTheme(R.style.Theme_WordWiz_Light)
    }
    super.onCreate(savedInstanceState)
    setContentView(R.layout.snackbar_screen)

    val layoutRoot = findViewById<ConstraintLayout>(R.id.content_root)
    Injector.obtain<WordWizComponent>(applicationContext)
        .plusMainComponent()
        .create(this, layoutRoot)
        .inject(this)

    val component = requireNotNull(view)
    val toolbarComponent = requireNotNull(toolbar)
    val dropshadow = requireNotNull(dropshadowView)

    component.inflate(savedInstanceState)
    toolbarComponent.inflate(savedInstanceState)
    dropshadow.inflate(savedInstanceState)

    this.doOnDestroy {
      component.teardown()
      toolbarComponent.teardown()
      dropshadow.teardown()
    }

    layoutRoot.layout {

      toolbarComponent.also {
        connect(it.id(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      dropshadow.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      component.also {
        connect(it.id(), ConstraintSet.TOP, toolbarComponent.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }
    }


    showPreferenceFragment()
  }

  private fun showPreferenceFragment() {
    val fragmentManager = supportFragmentManager
    val tag = SettingsFragment.TAG
    if (fragmentManager.findFragmentByTag(tag) == null && !AboutFragment.isPresent(this)
    ) {
      fragmentManager.commit(this) {
        add(fragmentContainerId, SettingsFragment(), tag)
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbar?.saveState(outState)
    view?.saveState(outState)
    dropshadowView?.saveState(outState)
  }

  override fun onDestroy() {
    super.onDestroy()
    view = null
    toolbar = null
    dropshadowView = null
  }
}
