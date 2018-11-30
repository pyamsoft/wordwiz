package com.pyamsoft.wordwiz.main

import com.pyamsoft.pydroid.ui.app.BaseView

interface MainPrefView : BaseView {

  fun onWordCountClicked(onClick: () -> Unit)

  fun onLetterCountClicked(onClick: () -> Unit)
}