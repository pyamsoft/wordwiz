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

package com.pyamsoft.wordwiz.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;
import com.pyamsoft.pydroid.ui.about.AboutLibrariesFragment;
import com.pyamsoft.pydroid.ui.rating.RatingDialog;
import com.pyamsoft.pydroid.ui.sec.TamperActivity;
import com.pyamsoft.pydroid.util.AppUtil;
import com.pyamsoft.wordwiz.BuildConfig;
import com.pyamsoft.wordwiz.R;
import com.pyamsoft.wordwiz.databinding.ActivityMainBinding;

public class MainActivity extends TamperActivity {

  private ActivityMainBinding binding;

  @Override protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.Theme_WordWiz_Light);
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    setupToolbarAsActionBar();
    showPreferenceFragment();
  }

  @Override protected int bindActivityToView() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    return R.id.ad_view;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    binding.unbind();
  }

  private void setupToolbarAsActionBar() {
    setSupportActionBar(binding.toolbar);
    binding.toolbar.setTitle(R.string.app_name);
    ViewCompat.setElevation(binding.toolbar, AppUtil.convertToDP(this, 4));
  }

  private void showPreferenceFragment() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    if (fragmentManager.findFragmentByTag(MainPreferenceFragment.TAG) == null
        && fragmentManager.findFragmentByTag(AboutLibrariesFragment.TAG) == null) {
      fragmentManager.beginTransaction()
          .add(R.id.main_view_container, new MainPreferenceFragment(), MainPreferenceFragment.TAG)
          .commitNow();
    }
  }

  @Override public void onBackPressed() {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final int backStackCount = fragmentManager.getBackStackEntryCount();
    if (backStackCount > 0) {
      fragmentManager.popBackStackImmediate();
    } else {
      super.onBackPressed();
    }
  }

  @Override public boolean onOptionsItemSelected(final @NonNull MenuItem item) {
    final int itemId = item.getItemId();
    boolean handled;
    switch (itemId) {
      case android.R.id.home:
        onBackPressed();
        handled = true;
        break;
      default:
        handled = false;
    }
    return handled || super.onOptionsItemSelected(item);
  }

  @NonNull @Override public String provideApplicationName() {
    return "WordWiz";
  }

  @NonNull @Override protected String[] getChangeLogLines() {
    final String line1 = "BUGFIX: Fixes related to in app billing code";
    final String line2 = "BUGFIX: Smaller memory footprint";
    final String line3 = "BUGFIX: Correctly close word count and letter count screens";
    return new String[] { line1, line2, line3 };
  }

  @NonNull @Override protected String getVersionName() {
    return BuildConfig.VERSION_NAME;
  }

  @Override protected void onPostResume() {
    super.onPostResume();
    RatingDialog.showRatingDialog(this, this);
  }

  @NonNull @Override protected String getSafePackageName() {
    return "com.pyamsoft.wordwiz";
  }

  @Override public int getApplicationIcon() {
    return R.mipmap.ic_launcher;
  }

  @Override public int getCurrentApplicationVersion() {
    return BuildConfig.VERSION_CODE;
  }
}
