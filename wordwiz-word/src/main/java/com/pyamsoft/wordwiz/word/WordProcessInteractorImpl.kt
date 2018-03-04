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

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.CheckResult
import com.pyamsoft.wordwiz.model.ProcessType.LETTER_COUNT
import com.pyamsoft.wordwiz.model.ProcessType.WORD_COUNT
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.Single
import timber.log.Timber

internal class WordProcessInteractorImpl internal constructor(
  context: Context
) : WordProcessCommonInteractor() {

  private val packageManager: PackageManager = context.packageManager
  private val labelTypeWordCount: String = context.getString(R.string.label_word_count)
  private val labelTypeLetterCount: String = context.getString(R.string.label_letter_count)
  private val labelTypeOccurrences: String = context.getString(R.string.label_occurrence_count)

  //@NonNull @Override public AsyncTask<Void, Void, WordProcessResult> getProcessType(
  //    @NonNull ComponentName componentName, @NonNull CharSequence text, @NonNull Bundle extras,
  //    @NonNull ActionSingle<WordProcessResult> onLoaded) {
  //}

  override fun getProcessType(
    componentName: ComponentName,
    text: CharSequence,
    extras: Bundle?
  ): Single<WordProcessResult> {
    return Single.fromCallable {
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
      return@fromCallable result
    }
  }

  @CheckResult
  private fun getProcessTypeForLabel(
    label: CharSequence,
    text: CharSequence
  ): WordProcessResult = when (label) {
    labelTypeWordCount -> WordProcessResult(WORD_COUNT, getWordCount(text))
    labelTypeLetterCount -> WordProcessResult(LETTER_COUNT, getLetterCount(text))
    labelTypeOccurrences -> throw RuntimeException("Not ready yet")
    else -> throw IllegalArgumentException("Invalid label: $label")
  }
}
