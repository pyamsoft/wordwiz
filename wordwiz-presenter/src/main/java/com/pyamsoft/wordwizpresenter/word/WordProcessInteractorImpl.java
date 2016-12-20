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

package com.pyamsoft.wordwizpresenter.word;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.tool.AsyncOffloader;
import com.pyamsoft.pydroid.tool.Offloader;
import com.pyamsoft.wordwizmodel.WordProcessResult;
import com.pyamsoft.wordwizpresenter.R;
import timber.log.Timber;

class WordProcessInteractorImpl extends WordProcessCommonInteractorImpl
    implements WordProcessInteractor {

  @SuppressWarnings("WeakerAccess") @NonNull final PackageManager packageManager;
  @NonNull private final String LABEL_TYPE_WORD_COUNT;
  @NonNull private final String LABEL_TYPE_LETTER_COUNT;
  @NonNull private final String LABEL_TYPE_OCCURRENCES;

  WordProcessInteractorImpl(@NonNull Context context) {
    packageManager = context.getPackageManager();

    // Label constants
    LABEL_TYPE_WORD_COUNT = context.getString(R.string.label_word_count);
    LABEL_TYPE_LETTER_COUNT = context.getString(R.string.label_letter_count);
    LABEL_TYPE_OCCURRENCES = context.getString(R.string.label_occurrence_count);
  }

  //@NonNull @Override public AsyncTask<Void, Void, WordProcessResult> getProcessType(
  //    @NonNull ComponentName componentName, @NonNull CharSequence text, @NonNull Bundle extras,
  //    @NonNull ActionSingle<WordProcessResult> onLoaded) {
  //}

  @NonNull @Override
  public Offloader<WordProcessResult> getProcessType(@NonNull ComponentName componentName,
      @NonNull CharSequence text, @NonNull Bundle extras) {
    return AsyncOffloader.newInstance(() -> {
      WordProcessResult result;
      try {
        Timber.d("Attempt to load the label this activity launched with");
        final ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 0);
        if (activityInfo == null) {
          Timber.e("Activity info is NULL");
          result = WordProcessResult.error();
        } else {
          final CharSequence label = activityInfo.loadLabel(packageManager);
          result = getProcessTypeForLabel(label, text);
        }
      } catch (PackageManager.NameNotFoundException e) {
        Timber.e(e, "Name not found ERROR");
        result = WordProcessResult.error();
      }

      return result;
    });
  }

  @SuppressWarnings("WeakerAccess") @CheckResult @NonNull WordProcessResult getProcessTypeForLabel(
      @NonNull CharSequence label, @NonNull CharSequence text) {
    final WordProcessResult result;
    if (label.equals(LABEL_TYPE_WORD_COUNT)) {
      result =
          WordProcessResult.create(WordProcessResult.ProcessType.WORD_COUNT, getWordCount(text));
    } else if (label.equals(LABEL_TYPE_LETTER_COUNT)) {
      result = WordProcessResult.create(WordProcessResult.ProcessType.LETTER_COUNT,
          getLetterCount(text));
    } else if (label.equals(LABEL_TYPE_OCCURRENCES)) {
      throw new RuntimeException("Not ready yet");
    } else {
      result = WordProcessResult.error();
    }

    return result;
  }
}
