package com.pyamsoft.wordwiz.word

import androidx.lifecycle.LifecycleOwner

class WordComponentImpl(
  private val owner: LifecycleOwner,
  private val wordProcessModule: WordProcessModule
) : WordComponent {

  override fun inject(activity: WordProcessActivity) {
    activity.viewModel = wordProcessModule.getViewModel(owner)
  }

}
