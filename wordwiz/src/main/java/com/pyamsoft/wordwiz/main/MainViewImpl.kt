package com.pyamsoft.wordwiz.main

import android.view.View
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.pyamsoft.pydroid.ui.app.activity.ActivityBase
import com.pyamsoft.pydroid.ui.util.DebouncedOnClickListener
import com.pyamsoft.pydroid.util.toDp
import com.pyamsoft.wordwiz.R
import com.pyamsoft.wordwiz.databinding.ActivityMainBinding

internal class MainViewImpl internal constructor(
  private val activity: ActivityBase
) : MainView, LifecycleObserver {

  private lateinit var binding: ActivityMainBinding

  init {
    activity.lifecycle.addObserver(this)
  }

  override fun create() {
    binding = DataBindingUtil.setContentView(activity, R.layout.activity_main)
    setupToolbar()
  }

  @Suppress("unused")
  @OnLifecycleEvent(ON_DESTROY)
  internal fun destroy() {
    activity.lifecycle.removeObserver(this)
    binding.unbind()
  }

  override fun root(): View {
    return binding.root
  }

  private fun setupToolbar() {
    binding.toolbar.apply {
      activity.setToolbar(this)
      setTitle(R.string.app_name)
      ViewCompat.setElevation(this, 4F.toDp(context).toFloat())
    }
  }

  override fun onToolbarNavClicked(onClick: () -> Unit) {
    binding.toolbar.setNavigationOnClickListener(DebouncedOnClickListener.create {
      onClick()
    })
  }

}