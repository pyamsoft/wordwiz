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

  private val packageManager: PackageManager = context.applicationContext.packageManager
  private val labelTypeWordCount: String = context.applicationContext.getString(
      R.string.label_word_count
  )
  private val labelTypeLetterCount: String = context.applicationContext.getString(
      R.string.label_letter_count
  )
  private val labelTypeOccurrences: String = context.applicationContext.getString(
      R.string.label_occurrence_count
  )

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
        throw RuntimeException("Name not found for ComponentName: " + componentName)
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
    else -> throw IllegalArgumentException("Invalid label: " + label)
  }
}
