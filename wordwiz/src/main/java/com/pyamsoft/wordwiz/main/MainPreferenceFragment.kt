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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyamsoft.pydroid.ui.app.fragment.SettingsPreferenceFragment
import com.pyamsoft.pydroid.ui.app.fragment.requireToolbarActivity
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.util.setUpEnabled
import com.pyamsoft.wordwiz.Injector
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.WordWizComponent
import com.pyamsoft.wordwiz.word.LetterCountActivity
import com.pyamsoft.wordwiz.word.WordCountActivity

class MainPreferenceFragment : SettingsPreferenceFragment() {

  internal lateinit var theming: Theming
  internal lateinit var rootView: MainPrefView

  override val rootViewContainer: Int = R.id.main_view_container

  override val preferenceXmlResId: Int = R.xml.preferences

  override val hideClearAll: Boolean = true

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    Injector.obtain<WordWizComponent>(requireContext().applicationContext)
        .plusMainPrefComponent(viewLifecycleOwner, preferenceScreen)
        .inject(this)

    rootView.create()
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    rootView.onWordCountClicked {
      val enabled = WordCountActivity.isEnabled(view.context)
      WordCountActivity.enable(view.context, !enabled)
    }

    rootView.onLetterCountClicked {
      val enabled = LetterCountActivity.isEnabled(view.context)
      LetterCountActivity.enable(view.context, !enabled)
    }
  }

  override fun onResume() {
    super.onResume()
    requireToolbarActivity().withToolbar {
      it.setTitle(R.string.app_name)
      it.setUpEnabled(false)
    }
  }

  companion object {

    const val TAG = "MainPreferenceFragment"
  }
}
