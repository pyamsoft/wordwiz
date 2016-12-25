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
import com.pyamsoft.pydroid.app.PersistLoader;
import com.pyamsoft.pydroid.util.PersistentCache;
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase;
import com.pyamsoft.wordwiz.presenter.word.WordProcessPresenter;
import com.pyamsoft.wordwiz.presenter.word.WordProcessPresenterLoader;
import java.util.Locale;
import timber.log.Timber;

public abstract class WordProcessActivity extends ActivityBase
    implements WordProcessPresenter.View {

  @NonNull private static final String KEY_PRESENTER = "key_word_process_presenter";
  @SuppressWarnings("WeakerAccess") WordProcessPresenter presenter;
  private long loadedKey;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    loadedKey = PersistentCache.get()
        .load(KEY_PRESENTER, savedInstanceState,
            new PersistLoader.Callback<WordProcessPresenter>() {
              @NonNull @Override public PersistLoader<WordProcessPresenter> createLoader() {
                return new WordProcessPresenterLoader();
              }

              @Override public void onPersistentLoaded(@NonNull WordProcessPresenter persist) {
                presenter = persist;
              }
            });

    handleIntent(getIntent());
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  @Override protected void onStart() {
    super.onStart();
    presenter.bindView(this);
  }

  @Override protected void onStop() {
    super.onStop();
    presenter.unbindView();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (!isChangingConfigurations()) {
      PersistentCache.get().unload(loadedKey);
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    PersistentCache.get().saveKey(outState, KEY_PRESENTER, loadedKey, WordProcessPresenter.class);
    super.onSaveInstanceState(outState);
  }

  private void handleIntent(@NonNull Intent intent) {
    Timber.d("Handle a process text intent");
    final CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
    presenter.handleActivityLaunchType(getComponentName(), text, getIntent().getExtras());
  }

  @Override public void onProcessComplete() {
    Timber.d("Process complete");
    finish();
  }

  @Override public void onProcessError() {
    Timber.e("An error occurred while attempting to process text");
    Toast.makeText(getApplicationContext(), "An error occurred while attempting to process text",
        Toast.LENGTH_SHORT).show();
  }

  @Override public void onProcessTypeWordCount(int wordCount) {
    Timber.d("Word count: %d", wordCount);
    Toast.makeText(getApplicationContext(), "Word count: " + wordCount, Toast.LENGTH_SHORT).show();
  }

  @Override public void onProcessTypeLetterCount(int letterCount) {
    Timber.d("Letter count: %d", letterCount);
    Toast.makeText(getApplicationContext(), "Letter count: " + letterCount, Toast.LENGTH_SHORT)
        .show();
  }

  @Override public void onProcessTypeOccurrences(int occurrences, @NonNull String snippet) {
    Timber.d("Occurrence count of snippet %s: %d", snippet, occurrences);
    Toast.makeText(getApplicationContext(),
        String.format(Locale.getDefault(), "Occurrence count of snippet %s: %d", snippet,
            occurrences), Toast.LENGTH_SHORT).show();
  }
}
