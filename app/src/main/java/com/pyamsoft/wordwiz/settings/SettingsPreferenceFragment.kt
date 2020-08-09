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

package com.pyamsoft.wordwiz.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.arch.viewModelFactory
import com.pyamsoft.pydroid.ui.settings.AppSettingsPreferenceFragment
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.settings.SettingsControllerEvent.LetterCountAction
import com.pyamsoft.wordwiz.settings.SettingsControllerEvent.WordCountAction
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity
import javax.inject.Inject

class SettingsPreferenceFragment : AppSettingsPreferenceFragment() {

    @JvmField
    @Inject
    internal var factory: ViewModelProvider.Factory? = null
    private val viewModel by viewModelFactory<SettingsViewModel> { factory }

    @JvmField
    @Inject
    internal var settingsView: SettingsView? = null

    @JvmField
    @Inject
    internal var toolbar: SettingsToolbarView? = null

    private var stateSaver: StateSaver? = null

    override val preferenceXmlResId: Int = R.xml.preferences

    override val hideClearAll: Boolean = true

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        Injector.obtain<WordWizComponent>(requireContext().applicationContext)
            .plusSettingsComponent()
            .create(requireToolbarActivity(), preferenceScreen)
            .inject(this)

        stateSaver = createComponent(
            savedInstanceState, viewLifecycleOwner,
            viewModel,
            requireNotNull(settingsView),
            requireNotNull(toolbar)
        ) {
            return@createComponent when (it) {
                is WordCountAction -> onWordCountChanged(it.isEnabled)
                is LetterCountAction -> onLetterCountChanged(it.isEnabled)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stateSaver?.saveState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateSaver = null
        factory = null
        settingsView = null
        toolbar = null
        factory = null
    }

    private fun onWordCountChanged(enabled: Boolean) {
        WordCountActivity.enable(requireContext(), enabled)
    }

    private fun onLetterCountChanged(enabled: Boolean) {
        LetterCountActivity.enable(requireContext(), enabled)
    }

    companion object {

        const val TAG = "SettingsPreferenceFragment"
    }
}
