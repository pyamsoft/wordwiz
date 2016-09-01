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

package com.pyamsoft.wordwiz.app.word;

import android.content.ComponentName;
import android.support.annotation.NonNull;
import com.pyamsoft.pydroid.base.presenter.Presenter;

public interface WordProcessPresenter extends Presenter<WordProcessPresenter.View> {

  void handleActivityLaunchType(@NonNull ComponentName componentName, @NonNull CharSequence text);

  interface View {

    void onProcessError(@NonNull CharSequence text);

    void onProcessTypeWordCount(int wordCount, @NonNull CharSequence text);
  }
}
