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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase;
import com.pyamsoft.wordwiz.Injector;
import java.util.Locale;
import timber.log.Timber;

public abstract class WordProcessActivity extends ActivityBase {

  @SuppressWarnings("WeakerAccess") WordProcessPresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    overridePendingTransition(0, 0);
    super.onCreate(savedInstanceState);
    Injector.get().provideComponent().plusWordProcessComponent().inject(this);

    handleIntent(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  @Override protected void onStop() {
    super.onStop();
    presenter.stop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    overridePendingTransition(0, 0);
    presenter.destroy();
  }

  private void handleIntent(@NonNull Intent intent) {
    Timber.d("Handle a process text intent");
    final CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
    presenter.handleActivityLaunchType(getComponentName(), text, getIntent().getExtras(),
        new WordProcessPresenter.ProcessCallback() {

          @Override public void onProcessBegin() {
            // Start
          }

          @Override public void onProcessError() {
            Timber.e("An error occurred while attempting to process text");
            Toast.makeText(getApplicationContext(),
                "An error occurred while attempting to process text", Toast.LENGTH_SHORT).show();
          }

          @Override public void onProcessComplete() {
            Timber.d("Process complete");
            finish();
          }

          @Override public void onProcessTypeWordCount(int wordCount) {
            Timber.d("Word count: %d", wordCount);
            Toast.makeText(getApplicationContext(), "Word count: " + wordCount, Toast.LENGTH_SHORT)
                .show();
          }

          @Override public void onProcessTypeLetterCount(int letterCount) {
            Timber.d("Letter count: %d", letterCount);
            Toast.makeText(getApplicationContext(), "Letter count: " + letterCount,
                Toast.LENGTH_SHORT).show();
          }

          @Override public void onProcessTypeOccurrences(int occurrences, @NonNull String snip) {
            Timber.d("Occurrence count of snippet %s: %d", snip, occurrences);
            Toast.makeText(getApplicationContext(),
                String.format(Locale.getDefault(), "Occurrence count of snippet %s: %d", snip,
                    occurrences), Toast.LENGTH_SHORT).show();
          }
        });
  }
}
