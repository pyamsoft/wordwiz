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

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ComponentManagerImpl @Inject internal constructor(private val context: Context) :
    ComponentManager {

  override fun isWordCountEnabled(): Boolean {
    return WordCountActivity.isEnabled(context)
  }

  override fun isLetterCountEnabled(): Boolean {
    return LetterCountActivity.isEnabled(context)
  }
}
