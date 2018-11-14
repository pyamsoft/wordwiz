package com.pyamsoft.wordwiz.word

import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.ui.theme.Theming

class WordComponentImpl(
  private val owner: LifecycleOwner,
  private val theming: Theming,
  private val wordProcessModule: WordProcessModule
) : WordComponent {

  override fun inject(activity: WordProcessActivity) {
    activity.theming = theming
    activity.viewModel = wordProcessModule.getViewModel(owner)
  }

}
