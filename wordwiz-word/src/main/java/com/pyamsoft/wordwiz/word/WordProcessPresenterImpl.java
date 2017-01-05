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

package com.pyamsoft.wordwiz.word;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.presenter.PresenterBase;
import com.pyamsoft.pydroid.tool.ExecutedOffloader;
import com.pyamsoft.pydroid.tool.OffloaderHelper;
import com.pyamsoft.wordwiz.model.WordProcessResult;
import timber.log.Timber;

class WordProcessPresenterImpl extends PresenterBase<WordProcessPresenter.View>
    implements WordProcessPresenter {

  @NonNull private final WordProcessInteractor interactor;
  @SuppressWarnings("WeakerAccess") @NonNull ExecutedOffloader activityLaunchTypeSubscription =
      new ExecutedOffloader.Empty();

  WordProcessPresenterImpl(@NonNull WordProcessInteractor interactor) {
    this.interactor = interactor;
  }

  @Override protected void onUnbind() {
    super.onUnbind();
    OffloaderHelper.cancel(activityLaunchTypeSubscription);
  }

  @Override public void handleActivityLaunchType(@NonNull ComponentName componentName,
      @NonNull CharSequence text, @NonNull Bundle extras) {
    OffloaderHelper.cancel(activityLaunchTypeSubscription);
    activityLaunchTypeSubscription =
        interactor.getProcessType(componentName, text, extras).onError(throwable -> {
          Timber.e(throwable, "onError handleActivityLaunchType");
          getView(View::onProcessError);
        }).onResult(this::handleProcessType).onFinish(() -> {
          OffloaderHelper.cancel(activityLaunchTypeSubscription);
          getView(View::onProcessComplete);
        }).execute();
  }

  @SuppressWarnings("WeakerAccess") void handleProcessType(@NonNull WordProcessResult processType) {
    final Bundle extras = processType.extras();
    switch (processType.type()) {
      case WORD_COUNT:
        getView(view -> view.onProcessTypeWordCount(processType.count()));
        break;
      case LETTER_COUNT:
        getView(view -> view.onProcessTypeLetterCount(processType.count()));
        break;
      case OCCURRENCES:
        if (extras == null) {
          throw new NullPointerException("Extras is NULL");
        }

        final String snippet = extras.getString(WordProcessResult.KEY_EXTRA_SNIPPET, null);
        if (snippet == null) {
          throw new NullPointerException("Snippet is NULL");
        }

        getView(view -> view.onProcessTypeOccurrences(processType.count(), snippet));
        break;
      case ERROR:
        getView(View::onProcessError);
    }
  }
}
