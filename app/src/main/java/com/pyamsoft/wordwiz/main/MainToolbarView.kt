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

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.ui.app.ToolbarActivityProvider
import com.pyamsoft.pydroid.ui.privacy.addPrivacy
import com.pyamsoft.pydroid.ui.privacy.removePrivacy
import com.pyamsoft.pydroid.ui.theme.ThemeProvider
import com.pyamsoft.pydroid.util.toDp
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWiz
import com.pyamsoft.wordwiz.databinding.ToolbarBinding
import javax.inject.Inject

internal class MainToolbarView @Inject internal constructor(
    parent: ViewGroup,
    theming: ThemeProvider,
    toolbarActivityProvider: ToolbarActivityProvider
) : BaseUiView<MainViewState, MainViewEvent, ToolbarBinding>(parent) {

    override val viewBinding = ToolbarBinding::inflate

    override val layoutRoot by boundView { toolbar }

    init {
        doOnInflate {
            val theme: Int = if (theming.isDarkTheme()) {
                R.style.ThemeOverlay_MaterialComponents
            } else {
                R.style.ThemeOverlay_MaterialComponents_Light
            }

            layoutRoot.apply {
                popupTheme = theme
                toolbarActivityProvider.setToolbar(this)
                setTitle(R.string.app_name)
                ViewCompat.setElevation(this, 4F.toDp(context).toFloat())
                viewScope.addPrivacy(
                    binding.toolbar,
                    WordWiz.PRIVACY_POLICY_URL,
                    WordWiz.TERMS_CONDITIONS_URL
                )
            }
        }

        doOnTeardown {
            toolbarActivityProvider.setToolbar(null)
            layoutRoot.removePrivacy()
        }
    }

    override fun onRender(state: MainViewState) {
    }
}
