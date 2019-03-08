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
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.ui.app.ToolbarActivityProvider
import com.pyamsoft.pydroid.util.toDp
import com.pyamsoft.wordwiz.R

internal class MainToolbarView internal constructor(
  parent: ViewGroup,
  private val toolbarActivityProvider: ToolbarActivityProvider
) : BaseUiView<Unit>(parent, Unit) {

  private val toolbar by lazyView<Toolbar>(R.id.toolbar)

  override val layout: Int = R.layout.toolbar

  override fun id(): Int {
    return toolbar.id
  }

  override fun onInflated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    setupToolbar()
  }

  private fun setupToolbar() {
    toolbar.apply {
      toolbarActivityProvider.setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, 4F.toDp(context).toFloat())
    }

  }

  override fun teardown() {
    toolbar.setNavigationOnClickListener(null)
  }

}
