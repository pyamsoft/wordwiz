/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.wordwiz.word

import androidx.annotation.CheckResult
import com.pyamsoft.wordwiz.api.WordProcessInteractor
import timber.log.Timber
import java.util.Arrays
import java.util.regex.Pattern

internal abstract class WordProcessCommonInteractor protected constructor() :
    WordProcessInteractor {

  @CheckResult
  private fun tokenizeString(text: CharSequence): Array<String> {
    Timber.d("Tokenize string by spaces")
    return text.toString()
        .split(
            SPLIT_BY_WHITESPACE.toRegex()
        )
        .dropLastWhile { it.isEmpty() }
        .toTypedArray()
  }

  @CheckResult
  fun getWordCount(text: CharSequence): Int {
    val tokens = tokenizeString(text)

    Timber.d("String tokenized: %s", Arrays.toString(tokens))
    return tokens.size
  }

  @CheckResult
  fun getLetterCount(text: CharSequence): Int {
    val tokens = tokenizeString(text)

    Timber.d("Get a sub of letter counts")
    return tokens.sumBy { it.length }
  }

  @CheckResult
  fun getOccurrences(
    text: CharSequence,
    snip: String
  ): Int {
    Timber.d("Find number of occurrences of %s in text:\n%s", snip, text)
    val pattern = Pattern.compile(snip, Pattern.LITERAL)
    val matcher = pattern.matcher(text)
    var count = 0
    while (matcher.find()) {
      ++count
    }

    return count
  }

  companion object {

    private const val SPLIT_BY_WHITESPACE = "\\s+"
  }
}
