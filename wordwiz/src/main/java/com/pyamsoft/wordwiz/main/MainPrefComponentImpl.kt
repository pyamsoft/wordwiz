package com.pyamsoft.wordwiz.main

import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.theme.Theming

internal class MainPrefComponentImpl internal constructor(
  private val theming: Theming,
  private val owner: LifecycleOwner,
  private val preferenceScreen: PreferenceScreen
) : MainPrefComponent {

  override fun inject(fragment: MainPreferenceFragment) {
    fragment.theming = theming
    fragment.rootView = MainPrefViewImpl(preferenceScreen, owner)
  }

}