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

package com.pyamsoft.wordwiz.word

import com.pyamsoft.pydroid.arch.UiControllerEvent
import com.pyamsoft.pydroid.arch.UiViewEvent
import com.pyamsoft.pydroid.arch.UiViewState

data class WordProcessState(
    val isProcessing: Processing?,
    val result: WordProcessResult?
) : UiViewState {

    data class Processing internal constructor(val isProcessing: Boolean)
}

sealed class WordProcessViewEvent : UiViewEvent {

    object CloseScreen : WordProcessViewEvent()
}

sealed class WordProcessControllerEvent : UiControllerEvent {

    object Finish : WordProcessControllerEvent()

    data class Error internal constructor(val throwable: Throwable) : WordProcessControllerEvent()
}
