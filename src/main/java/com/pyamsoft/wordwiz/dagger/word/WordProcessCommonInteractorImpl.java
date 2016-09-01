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

package com.pyamsoft.wordwiz.dagger.word;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import java.util.Arrays;
import timber.log.Timber;

abstract class WordProcessCommonInteractorImpl {

  @NonNull private static final String SPLIT_BY_WHITESPACE = "\\s+";

  @CheckResult int getWordCount(@NonNull CharSequence text) {
    Timber.d("Convert text to a string");
    final String string = text.toString();

    Timber.d("Tokenize string by spaces");
    final String[] tokens = string.split(SPLIT_BY_WHITESPACE);

    Timber.d("String tokenized: %s", Arrays.toString(tokens));
    return tokens.length;
  }
}
