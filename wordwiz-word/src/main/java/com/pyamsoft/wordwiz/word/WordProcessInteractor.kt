/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.word

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.CheckResult
import com.pyamsoft.wordwiz.model.ProcessType
import com.pyamsoft.wordwiz.model.WordProcessResult
import io.reactivex.Single
import timber.log.Timber

internal class WordProcessInteractor internal constructor(
    context: Context) : WordProcessCommonInteractor() {

  val packageManager: PackageManager = context.applicationContext.packageManager
  private val LABEL_TYPE_WORD_COUNT: String = context.applicationContext.getString(
      R.string.label_word_count)
  private val LABEL_TYPE_LETTER_COUNT: String = context.applicationContext.getString(
      R.string.label_letter_count)
  private val LABEL_TYPE_OCCURRENCES: String = context.applicationContext.getString(
      R.string.label_occurrence_count)

  //@NonNull @Override public AsyncTask<Void, Void, WordProcessResult> getProcessType(
  //    @NonNull ComponentName componentName, @NonNull CharSequence text, @NonNull Bundle extras,
  //    @NonNull ActionSingle<WordProcessResult> onLoaded) {
  //}

  /**
   * public
   */
  @CheckResult fun getProcessType(
      componentName: ComponentName, text: CharSequence,
      extras: Bundle?): Single<WordProcessResult> {
    return Single.fromCallable {
      val result: WordProcessResult
      try {
        Timber.d("Attempt to load the label this activity launched with")
        val activityInfo = packageManager.getActivityInfo(componentName, 0)
        if (activityInfo == null) {
          Timber.e("Activity info is NULL")
          throw RuntimeException("ActivityInfo is NULL")
        } else {
          val label = activityInfo.loadLabel(packageManager)
          result = getProcessTypeForLabel(label, text)
        }
      } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e, "Name not found ERROR")
        throw RuntimeException("Name not found for ComponentName: " + componentName)
      }

      result
    }
  }

  @CheckResult fun getProcessTypeForLabel(
      label: CharSequence, text: CharSequence): WordProcessResult {
    val result: WordProcessResult
    if (label == LABEL_TYPE_WORD_COUNT) {
      result = WordProcessResult(ProcessType.WORD_COUNT, getWordCount(text))
    } else if (label == LABEL_TYPE_LETTER_COUNT) {
      result = WordProcessResult(ProcessType.LETTER_COUNT, getLetterCount(text))
    } else if (label == LABEL_TYPE_OCCURRENCES) {
      throw RuntimeException("Not ready yet")
    } else {
      throw IllegalArgumentException("Invalid label: " + label)
    }

    return result
  }
}
