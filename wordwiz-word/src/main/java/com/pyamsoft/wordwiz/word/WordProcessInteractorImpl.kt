/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.wordwiz.word

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.core.Enforcer
import com.pyamsoft.wordwiz.word.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.word.ProcessType.WORD_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

internal class WordProcessInteractorImpl @Inject internal constructor(
    context: Context,
    private val enforcer: Enforcer
) : WordProcessInteractor {

    private val packageManager: PackageManager = context.packageManager
    private val labelTypeWordCount: String = context.getString(R.string.label_word_count)
    private val labelTypeLetterCount: String = context.getString(R.string.label_letter_count)
    private val labelTypeOccurrences: String = context.getString(R.string.label_occurrence_count)

    @CheckResult
    private fun tokenizeString(text: CharSequence): Array<String> {
        enforcer.assertNotOnMainThread()
        Timber.d("Tokenize string by spaces")
        return text.toString()
            .split(SPLIT_BY_WHITESPACE.toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    @CheckResult
    private fun getWordCount(text: CharSequence): Int {
        enforcer.assertNotOnMainThread()
        val tokens = tokenizeString(text)

        Timber.d("String tokenized: %s", Arrays.toString(tokens))
        return tokens.size
    }

    @CheckResult
    private fun getLetterCount(text: CharSequence): Int {
        enforcer.assertNotOnMainThread()
        val tokens = tokenizeString(text)

        Timber.d("Get a sub of letter counts")
        return tokens.sumBy { it.length }
    }

    @CheckResult
    private fun getOccurrences(
        text: CharSequence,
        snip: String
    ): Int {
        enforcer.assertNotOnMainThread()
        Timber.d("Find number of occurrences of %s in text:\n%s", snip, text)
        val pattern = Pattern.compile(snip, Pattern.LITERAL)
        val matcher = pattern.matcher(text)
        var count = 0
        while (matcher.find()) {
            ++count
        }

        return count
    }

    override suspend fun getProcessType(
        componentName: ComponentName,
        text: CharSequence
    ): WordProcessResult = withContext(context = Dispatchers.Default) {
        enforcer.assertNotOnMainThread()
        val result: WordProcessResult
        try {
            Timber.d("Attempt to load the label this activity launched with")
            val activityInfo: ActivityInfo = packageManager.getActivityInfo(componentName, 0)
            val label = activityInfo.loadLabel(packageManager)
            result = getProcessTypeForLabel(label, text)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Name not found ERROR")
            throw RuntimeException("Name not found for ComponentName: $componentName")
        }

        return@withContext result
    }

    @CheckResult
    private fun getProcessTypeForLabel(
        label: CharSequence,
        text: CharSequence
    ): WordProcessResult {
        enforcer.assertNotOnMainThread()
        return when (label) {
            labelTypeWordCount -> WordProcessResult(
                WORD_COUNT, getWordCount(text)
            )
            labelTypeLetterCount -> WordProcessResult(
                LETTER_COUNT, getLetterCount(text)
            )
            labelTypeOccurrences -> throw RuntimeException("Not ready yet")
            else -> throw IllegalArgumentException("Invalid label: $label")
        }
    }

    companion object {

        private const val SPLIT_BY_WHITESPACE = "\\s+"
    }
}
