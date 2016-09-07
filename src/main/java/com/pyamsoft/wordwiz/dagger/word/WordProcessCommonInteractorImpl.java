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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import timber.log.Timber;

abstract class WordProcessCommonInteractorImpl {

  @NonNull private static final String SPLIT_BY_WHITESPACE = "\\s+";

  @NonNull @CheckResult String[] tokenizeString(@NonNull CharSequence text) {
    Timber.d("Convert text to a string");
    final String string = text.toString();

    Timber.d("Tokenize string by spaces");
    final String[] tokens = string.split(SPLIT_BY_WHITESPACE);

    return tokens;
  }

  @CheckResult int getWordCount(@NonNull CharSequence text) {
    final String[] tokens = tokenizeString(text);

    Timber.d("String tokenized: %s", Arrays.toString(tokens));
    return tokens.length;
  }

  @CheckResult int getLetterCount(@NonNull CharSequence text) {
    final String[] tokens = tokenizeString(text);

    Timber.d("Get a sub of letter counts");
    int sum = 0;
    for (final String token : tokens) {
      sum += token.length();
    }

    return sum;
  }

  @CheckResult int getOccurrences(@NonNull CharSequence text, @NonNull String snip) {
    Timber.d("Find number of occurrences of %s in text:\n%s", snip, text);
    final Pattern pattern = Pattern.compile(snip, Pattern.LITERAL);
    final Matcher matcher = pattern.matcher(text);
    int count = 0;
    while (matcher.find()) {
      ++count;
    }

    return count;
  }
}
