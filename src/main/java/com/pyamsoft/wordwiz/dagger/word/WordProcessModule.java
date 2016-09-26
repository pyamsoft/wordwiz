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

import android.content.Context;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.ActivityScope;
import com.pyamsoft.wordwiz.app.word.WordProcessPresenter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import rx.Scheduler;

@Module public class WordProcessModule {

  @ActivityScope @Provides WordProcessPresenter provideWordProcessPresenter(
      @NonNull WordProcessInteractor interactor, @Named("main") Scheduler observeScheduler,
      @Named("computation") Scheduler subcribeScheduler) {
    return new WordProcessPresenterImpl(interactor, observeScheduler, subcribeScheduler);
  }

  @ActivityScope @Provides WordProcessInteractor provideWordProcessInteractor(
      @NonNull Context context) {
    return new WordProcessInteractorImpl(context);
  }
}
