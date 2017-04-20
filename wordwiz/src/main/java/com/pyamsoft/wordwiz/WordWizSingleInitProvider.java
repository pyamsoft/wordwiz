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

package com.pyamsoft.wordwiz;

import android.content.Context;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.about.Licenses;
import com.pyamsoft.pydroid.helper.BuildConfigChecker;
import com.pyamsoft.pydroid.ui.SingleInitContentProvider;
import com.pyamsoft.wordwiz.base.WordWizModule;

public class WordWizSingleInitProvider extends SingleInitContentProvider {

  @NonNull @Override protected BuildConfigChecker initializeBuildConfigChecker() {
    return new BuildConfigChecker() {
      @Override public boolean isDebugMode() {
        return BuildConfig.DEBUG;
      }
    };
  }

  @Override protected void onInstanceCreated(@NonNull Context context) {
    final WordWizComponent component = WordWizComponent.withModule(new WordWizModule(context));
    Injector.set(component);
  }

  @Override protected void insertCustomLicensesIntoMap(@NonNull Context context) {
    super.insertCustomLicensesIntoMap(context);
    Licenses.create("Firebase", "https://firebase.google.com", "licenses/firebase");
  }
}
