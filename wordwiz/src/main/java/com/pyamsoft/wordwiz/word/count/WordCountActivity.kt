/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.word.count

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.pyamsoft.wordwiz.word.WordProcessActivity
import timber.log.Timber

class WordCountActivity : WordProcessActivity() {

  companion object {

    @JvmStatic
    fun enable(context: Context, enable: Boolean) {
      Timber.d("set WordCountActivity enabled state: %s", enable)
      val cmp = ComponentName(context.applicationContext, WordCountActivity::class.java)
      val componentState = if (enable)
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
      else
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
      context.applicationContext
          .packageManager
          .setComponentEnabledSetting(cmp, componentState, PackageManager.DONT_KILL_APP)
    }

    @JvmStatic
    fun isEnabled(context: Context): Boolean {
      val cmp = ComponentName(context.applicationContext, WordCountActivity::class.java)
      val componentState = context.applicationContext.packageManager.getComponentEnabledSetting(cmp)
      return componentState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }
  }
}
