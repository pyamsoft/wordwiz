package com.pyamsoft.wordwiz.main

import com.pyamsoft.pydroid.ui.app.BaseScreen

interface MainView : BaseScreen {

  fun onToolbarNavClicked(onClick: () -> Unit)
}