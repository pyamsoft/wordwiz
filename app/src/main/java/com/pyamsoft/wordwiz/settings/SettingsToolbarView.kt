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

package com.pyamsoft.wordwiz.settings

import com.pyamsoft.pydroid.arch.UiRender
import com.pyamsoft.pydroid.arch.UiView
import com.pyamsoft.pydroid.ui.app.ToolbarActivity
import com.pyamsoft.pydroid.ui.util.setUpEnabled
import com.pyamsoft.wordwiz.R
import javax.inject.Inject

internal class SettingsToolbarView @Inject internal constructor(toolbarActivity: ToolbarActivity) :
    UiView<SettingsViewState, SettingsViewEvent>() {

  init {
    toolbarActivity.withToolbar { toolbar ->
      toolbar.setTitle(R.string.app_name)
      toolbar.setUpEnabled(false)
    }
  }

  override fun render(state: UiRender<SettingsViewState>) {}
}
