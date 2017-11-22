/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.wordwiz.word.count

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.pyamsoft.wordwiz.word.WordProcessActivity
import timber.log.Timber

class LetterCountActivity : WordProcessActivity() {

    companion object {

        @JvmStatic
        fun enable(context: Context, enable: Boolean) {
            Timber.d("set LetterCountActivity enabled state: %s", enable)
            val cmp = ComponentName(context.applicationContext, LetterCountActivity::class.java)
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
            val cmp = ComponentName(context.applicationContext, LetterCountActivity::class.java)
            val componentState = context.applicationContext.packageManager.getComponentEnabledSetting(
                    cmp)
            return componentState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        }
    }
}
