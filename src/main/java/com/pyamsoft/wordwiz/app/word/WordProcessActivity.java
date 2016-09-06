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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.pyamsoft.pydroid.base.ActivityBase;
import com.pyamsoft.pydroid.base.PersistLoader;
import com.pyamsoft.pydroid.util.PersistentCache;
import timber.log.Timber;

public abstract class WordProcessActivity extends ActivityBase implements WordProcessPresenter.View {

  @NonNull private static final String KEY_PRESENTER = "key_word_process_presenter";
  WordProcessPresenter presenter;
  private long loadedKey;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    loadedKey = PersistentCache.load(KEY_PRESENTER, savedInstanceState,
        new PersistLoader.Callback<WordProcessPresenter>() {
          @NonNull @Override public PersistLoader<WordProcessPresenter> createLoader() {
            return new WordProcessPresenterLoader(getApplicationContext());
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
      PersistentCache.unload(loadedKey);
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    PersistentCache.saveKey(outState, KEY_PRESENTER, loadedKey);
    super.onSaveInstanceState(outState);
  }

  private void handleIntent(@NonNull Intent intent) {
    Timber.d("Handle a process text intent");
    final CharSequence text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
    presenter.handleActivityLaunchType(getComponentName(), text);
  }

  @Override public void onProcessTypeWordCount(int wordCount, @NonNull CharSequence text) {
    Timber.d("Word count: %d", wordCount);
    Toast.makeText(getApplicationContext(), "Word count: " + wordCount, Toast.LENGTH_SHORT).show();
    finish();
  }

  @Override public void onProcessError(@NonNull CharSequence text) {
    Timber.e("An error occurred while attempting to process text");
    Toast.makeText(getApplicationContext(), "An error occurred while attempting to process text",
        Toast.LENGTH_SHORT).show();
    finish();
  }
}
