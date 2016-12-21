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

package com.pyamsoft.wordwizmodel;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class WordProcessResult {

  @NonNull public static final String KEY_EXTRA_SNIPPET = "key_extra_snipper";

  @CheckResult @NonNull
  public static WordProcessResult create(@NonNull ProcessType type, int wordCount) {
    return create(type, wordCount, null);
  }

  @SuppressWarnings("WeakerAccess") @CheckResult @NonNull
  public static WordProcessResult create(@NonNull ProcessType type, int wordCount,
      @SuppressWarnings("SameParameterValue") @Nullable Bundle extras) {
    return new AutoValue_WordProcessResult(type, wordCount, extras);
  }

  @CheckResult @NonNull public static WordProcessResult error() {
    return create(ProcessType.ERROR, -1);
  }

  public abstract ProcessType type();

  public abstract int count();

  @Nullable public abstract Bundle extras();
}
