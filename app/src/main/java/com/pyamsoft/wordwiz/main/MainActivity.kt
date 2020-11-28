/*
 * Copyright 2020 Peter Kenji Yamanaka
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

import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.arch.viewModelFactory
import com.pyamsoft.pydroid.ui.changelog.ChangeLogActivity
import com.pyamsoft.pydroid.ui.changelog.buildChangeLog
import com.pyamsoft.pydroid.ui.theme.ThemeProvider
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.ui.util.layout
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import com.pyamsoft.pydroid.util.stableLayoutHideNavigation
import com.pyamsoft.wordwiz.BuildConfig
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.settings.SettingsFragment
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : ChangeLogActivity() {

    @JvmField
    @Inject
    internal var toolbar: MainToolbarView? = null

    @JvmField
    @Inject
    internal var view: MainFrameView? = null

    @JvmField
    @Inject
    internal var theming: Theming? = null

    @JvmField
    @Inject
    internal var factory: ViewModelProvider.Factory? = null
    private val viewModel by viewModelFactory<MainViewModel> { factory }

    private var stateSaver: StateSaver? = null

    override val versionName: String = BuildConfig.VERSION_NAME

    override val applicationIcon: Int = R.mipmap.ic_launcher

    override val snackbarRoot: ViewGroup by lazy(NONE) {
        findViewById<CoordinatorLayout>(R.id.snackbar_root)
    }

    override val fragmentContainerId: Int
        get() = requireNotNull(view).id()

    override val changelog = buildChangeLog {
        change("Lower memory consumption and faster operation")
        bugfix("Fixed a memory leak in the license viewing experience")
        feature("Added links to Terms of Service and Privacy Policy")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WordWiz)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.snackbar_screen)

        val layoutRoot = findViewById<ConstraintLayout>(R.id.content_root)
        Injector.obtain<WordWizComponent>(applicationContext)
            .plusMainComponent()
            .create(
                this,
                layoutRoot,
                ThemeProvider { requireNotNull(theming).isDarkTheme(this) }
            )
            .inject(this)

        val component = requireNotNull(view)
        val toolbarComponent = requireNotNull(toolbar)
        val dropshadow = DropshadowView.createTyped<MainViewState, MainViewEvent>(layoutRoot)

        stableLayoutHideNavigation()

        stateSaver = createComponent(
            savedInstanceState, this,
            viewModel,
            component,
            toolbarComponent,
            dropshadow
        ) {}

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
                connect(
                    it.id(),
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
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
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentManager.commit(this) {
                add(fragmentContainerId, SettingsFragment(), tag)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stateSaver?.saveState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        factory = null
        stateSaver = null
        view = null
        toolbar = null
    }
}
