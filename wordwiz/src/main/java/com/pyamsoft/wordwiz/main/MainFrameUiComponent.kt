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
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.ui.arch.UiComponent
import com.pyamsoft.pydroid.ui.arch.ViewEvent.EMPTY
import io.reactivex.Observable

internal class MainFrameUiComponent internal constructor(
  private val frameView: MainFrameView,
  owner: LifecycleOwner
) : UiComponent<EMPTY>(owner) {

  override fun id(): Int {
    return frameView.id()
  }

  override fun create(savedInstanceState: Bundle?) {
    frameView.inflate(savedInstanceState)
    owner.runOnDestroy { frameView.teardown() }
  }

  override fun onUiEvent(): Observable<EMPTY> {
    return Observable.empty()
  }

  override fun saveState(outState: Bundle) {
    frameView.saveState(outState)
  }

}