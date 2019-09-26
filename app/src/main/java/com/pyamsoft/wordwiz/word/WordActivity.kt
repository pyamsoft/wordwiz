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

package com.pyamsoft.wordwiz.word

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.CheckResult
import timber.log.Timber

internal abstract class WordActivity<out T : WordProcessActivity> {

    @CheckResult
    abstract fun provideClassType(): Class<out T>

    fun enable(
        context: Context,
        enable: Boolean
    ) {
        enable(context, enable, provideClassType())
    }

    @CheckResult
    fun isEnabled(context: Context): Boolean {
        return isEnabled(context, provideClassType())
    }

    private fun enable(
        context: Context,
        enable: Boolean,
        targetClass: Class<out T>
    ) {
        Timber.d("set ${targetClass.simpleName} enabled state: $enable")
        val name = ComponentName(context, targetClass)
        val state = if (enable) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }

        context.packageManager.setComponentEnabledSetting(name, state, PackageManager.DONT_KILL_APP)
    }

    @CheckResult
    private fun isEnabled(
        context: Context,
        targetClass: Class<out T>
    ): Boolean {
        val name = ComponentName(context, targetClass)
        val state = context.packageManager.getComponentEnabledSetting(name)
        return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }
}
