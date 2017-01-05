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

package com.pyamsoft.wordwiz.base;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.pyamsoft.pydroid.IPYDroidApp;
import com.pyamsoft.pydroid.SingleInitContentProvider;

public abstract class BaseInitProvider extends SingleInitContentProvider
    implements IPYDroidApp<WordWizModule> {

  @Nullable private WordWizModule module;

  @CallSuper @Override protected void onInstanceCreated(@NonNull Context context) {
    Injector.set(module);
  }

  @CallSuper @Override protected void onFirstCreate(@NonNull Context context) {
    super.onFirstCreate(context);
    module = new WordWizModule(context);
  }

  @NonNull @Override public final WordWizModule provideComponent() {
    if (module == null) {
      throw new NullPointerException("WordWiz module is NULL");
    }
    return module;
  }
}
