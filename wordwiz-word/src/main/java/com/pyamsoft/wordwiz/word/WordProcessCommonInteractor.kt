/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.wordwiz.word

import android.support.annotation.CheckResult
import timber.log.Timber
import java.util.Arrays
import java.util.regex.Pattern

internal abstract class WordProcessCommonInteractor protected constructor() : WordProcessInteractor {

  @CheckResult private fun tokenizeString(text: CharSequence): Array<String> {
    Timber.d("Tokenize string by spaces")
    return text.toString().split(
        SPLIT_BY_WHITESPACE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
  }

  @CheckResult fun getWordCount(text: CharSequence): Int {
    val tokens = tokenizeString(text)

    Timber.d("String tokenized: %s", Arrays.toString(tokens))
    return tokens.size
  }

  @CheckResult fun getLetterCount(text: CharSequence): Int {
    val tokens = tokenizeString(text)

    Timber.d("Get a sub of letter counts")
    return tokens.sumBy { it.length }
  }

  @CheckResult fun getOccurrences(text: CharSequence, snip: String): Int {
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
