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

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.pyamsoft.pydroid.ui.IPYDroidApp;

public class Injector implements IPYDroidApp<WordWizComponent> {

  @Nullable private static volatile Injector instance = null;
  @NonNull private final WordWizComponent component;

  private Injector(@NonNull WordWizComponent component) {
    this.component = component;
  }

  static void set(@Nullable WordWizComponent component) {
    if (component == null) {
      throw new NullPointerException("Cannot set a NULL component");
    }
    instance = new Injector(component);
  }

  @NonNull @CheckResult public static Injector get() {
    if (instance == null) {
      throw new NullPointerException("Instance is NULL");
    }

    //noinspection ConstantConditions
    return instance;
  }

  @NonNull @Override public WordWizComponent provideComponent() {
    return component;
  }
}