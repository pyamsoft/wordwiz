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
import com.pyamsoft.wordwiz.base.WordWizModule;
import com.pyamsoft.wordwiz.word.WordProcessComponent;

public class WordWizComponent {

  @NonNull private final WordProcessComponent wordProcessComponent;

  WordWizComponent(@NonNull WordWizModule module) {
    wordProcessComponent = new WordProcessComponent(module);
  }

  @CheckResult @NonNull static Builder builder() {
    return new Builder();
  }

  @CheckResult @NonNull public WordProcessComponent plusWordProcessComponent() {
    return wordProcessComponent;
  }

  static class Builder {

    private WordWizModule module;

    Builder() {
    }

    @CheckResult @NonNull Builder wordWizModule(@NonNull WordWizModule module) {
      this.module = module;
      return this;
    }

    @CheckResult @NonNull WordWizComponent build() {
      if (module == null) {
        throw new IllegalStateException("WordWizModule cannot be NULL");
      }

      return new WordWizComponent(module);
    }
  }
}
