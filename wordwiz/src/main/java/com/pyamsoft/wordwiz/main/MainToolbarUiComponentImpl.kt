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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView

internal class MainToolbarUiComponentImpl internal constructor(
  private val toolbar: MainToolbarView,
  private val dropshadow: DropshadowView
) : BaseUiComponent<Unit>(),
    MainToolbarUiComponent {

  override fun id(): Int {
    return toolbar.id()
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Unit
  ) {
    owner.doOnDestroy {
      toolbar.teardown()
      dropshadow.teardown()
    }

    toolbar.inflate(savedInstanceState)
    dropshadow.inflate(savedInstanceState)
  }

  override fun saveState(outState: Bundle) {
    toolbar.saveState(outState)
    dropshadow.saveState(outState)
  }

  override fun layout(constraintLayout: ConstraintLayout) {
    ConstraintSet().apply {
      clone(constraintLayout)

      toolbar.also {
        connect(it.id(), ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP)
        connect(it.id(), ConstraintSet.START, constraintLayout.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, constraintLayout.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      dropshadow.also {
        connect(it.id(), ConstraintSet.TOP, toolbar.id(), ConstraintSet.BOTTOM)
        connect(it.id(), ConstraintSet.START, constraintLayout.id, ConstraintSet.START)
        connect(it.id(), ConstraintSet.END, constraintLayout.id, ConstraintSet.END)
        constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      }

      applyTo(constraintLayout)
    }
  }

}