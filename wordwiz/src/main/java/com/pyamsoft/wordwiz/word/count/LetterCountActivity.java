/*
 * Copyright 2016 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.word.count;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.helper.Checker;
import com.pyamsoft.wordwiz.word.WordProcessActivity;
import timber.log.Timber;

public class LetterCountActivity extends WordProcessActivity {

  public LetterCountActivity() {
  }

  public static void enable(@NonNull Context context, boolean enable) {
    Timber.d("set LetterCountActivity enabled state: %s", enable);
    context = Checker.checkNonNull(context);
    final ComponentName cmp =
        new ComponentName(context.getApplicationContext(), LetterCountActivity.class);
    final int componentState = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    context.getApplicationContext()
        .getPackageManager()
        .setComponentEnabledSetting(cmp, componentState, PackageManager.DONT_KILL_APP);
  }

  public static boolean isEnabled(@NonNull Context context) {
    context = Checker.checkNonNull(context);
    final ComponentName cmp =
        new ComponentName(context.getApplicationContext(), LetterCountActivity.class);
    final int componentState =
        context.getApplicationContext().getPackageManager().getComponentEnabledSetting(cmp);
    return componentState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
  }
}
