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

import android.content.ComponentName;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.dagger.presenter.SchedulerPresenter;
import com.pyamsoft.wordwiz.app.word.WordProcessPresenter;
import com.pyamsoft.wordwiz.model.WordProcessResult;
import javax.inject.Inject;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

class WordProcessPresenterImpl extends SchedulerPresenter<WordProcessPresenter.View>
    implements WordProcessPresenter {

  @NonNull private final WordProcessInteractor interactor;
  @NonNull private Subscription activityLaunchTypeSubscription = Subscriptions.empty();

  @Inject WordProcessPresenterImpl(@NonNull WordProcessInteractor interactor,
      @NonNull Scheduler observeScheduler, @NonNull Scheduler subscribeScheduler) {
    super(observeScheduler, subscribeScheduler);
    this.interactor = interactor;
  }

  @Override protected void onUnbind() {
    super.onUnbind();
    unsubActivityLaunchType();
  }

  @Override public void handleActivityLaunchType(@NonNull ComponentName componentName,
      @NonNull CharSequence text) {
    unsubActivityLaunchType();
    activityLaunchTypeSubscription = interactor.getProcessType(componentName, text)
        .subscribeOn(getSubscribeScheduler())
        .observeOn(getObserveScheduler())
        .subscribe(this::handleProcessType, throwable -> {
          Timber.e(throwable, "onError handleActivityLaunchType");
          getView().onProcessError(text);
        }, this::unsubActivityLaunchType);
  }

  void handleProcessType(@NonNull WordProcessResult processType) {
    switch (processType.type()) {
      case WORD_COUNT:
        getView().onProcessTypeWordCount(processType.count(), processType.text());
        break;
      case ERROR:
        getView().onProcessError(processType.text());
    }
  }

  void unsubActivityLaunchType() {
    if (!activityLaunchTypeSubscription.isUnsubscribed()) {
      activityLaunchTypeSubscription.unsubscribe();
    }
  }
}
