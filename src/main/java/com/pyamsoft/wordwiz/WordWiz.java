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

package com.pyamsoft.wordwiz;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.lib.PYDroidApplication;
import com.pyamsoft.wordwiz.dagger.DaggerWordWizComponent;
import com.pyamsoft.wordwiz.dagger.WordWizComponent;
import com.pyamsoft.wordwiz.dagger.WordWizModule;

public class WordWiz extends PYDroidApplication implements IWordWiz<WordWizComponent> {

  private WordWizComponent component;

  @NonNull @CheckResult public static IWordWiz get(@NonNull Context context) {
    final Context appContext = context.getApplicationContext();
    if (appContext instanceof IWordWiz) {
      return WordWiz.class.cast(appContext);
    } else {
      throw new ClassCastException("Cannot cast Application Context to IWordWiz");
    }
  }

  @Override protected void onFirstCreate() {
    super.onFirstCreate();
    component = DaggerWordWizComponent.builder()
        .wordWizModule(new WordWizModule(getApplicationContext()))
        .build();
  }

  @NonNull @Override public WordWizComponent provideComponent() {
    if (component == null) {
      throw new NullPointerException("WordWiz component is NULL");
    }
    return component;
  }
}
