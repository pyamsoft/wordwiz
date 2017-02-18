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
import android.support.annotation.RestrictTo;
import com.pyamsoft.pydroid.helper.SubscriptionHelper;
import com.pyamsoft.pydroid.presenter.Presenter;
import com.pyamsoft.pydroid.presenter.SchedulerPresenter;
import com.pyamsoft.wordwiz.model.WordProcessResult;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

class WordProcessPresenter extends SchedulerPresenter<Presenter.Empty> {

  @NonNull private final WordProcessInteractor interactor;
  @NonNull private Subscription subscription = Subscriptions.empty();

  WordProcessPresenter(@NonNull WordProcessInteractor interactor,
      @NonNull Scheduler observeScheduler, @NonNull Scheduler subscribeScheduler) {
    super(observeScheduler, subscribeScheduler);
    this.interactor = interactor;
  }

  @Override protected void onUnbind() {
    super.onUnbind();
    subscription = SubscriptionHelper.unsubscribe(subscription);
  }

  void handleActivityLaunchType(@NonNull ComponentName componentName, @NonNull CharSequence text,
      @NonNull Bundle extras, @NonNull ProcessCallback callback) {
    subscription = SubscriptionHelper.unsubscribe(subscription);
    subscription = interactor.getProcessType(componentName, text, extras)
        .subscribeOn(getSubscribeScheduler())
        .observeOn(getObserveScheduler())
        .doAfterTerminate(callback::onProcessComplete)
        .subscribe(wordProcessResult -> handleProcessType(wordProcessResult, callback),
            throwable -> {
              Timber.e(throwable, "onError handleActivityLaunchType");
              callback.onProcessError();
            });
  }

  /**
   * @hide
   */
  @RestrictTo(RestrictTo.Scope.SUBCLASSES) @SuppressWarnings("WeakerAccess") void handleProcessType(
      @NonNull WordProcessResult processType, @NonNull ProcessCallback callback) {
    final Bundle extras = processType.extras();
    switch (processType.type()) {
      case WORD_COUNT:
        callback.onProcessTypeWordCount(processType.count());
        break;
      case LETTER_COUNT:
        callback.onProcessTypeLetterCount(processType.count());
        break;
      case OCCURRENCES:
        if (extras == null) {
          throw new NullPointerException("Extras is NULL");
        }

        final String snippet = extras.getString(WordProcessResult.KEY_EXTRA_SNIPPET, null);
        if (snippet == null) {
          throw new NullPointerException("Snippet is NULL");
        }

        callback.onProcessTypeOccurrences(processType.count(), snippet);
        break;
      case ERROR:
        callback.onProcessError();
    }
  }

  interface ProcessCallback {

    void onProcessError();

    void onProcessComplete();

    void onProcessTypeWordCount(int wordCount);

    void onProcessTypeLetterCount(int letterCount);

    void onProcessTypeOccurrences(int occurrences, @NonNull String snip);
  }
}
