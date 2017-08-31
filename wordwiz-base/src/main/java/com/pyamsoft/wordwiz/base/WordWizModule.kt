/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.wordwiz.base

import android.content.Context
import android.support.annotation.CheckResult
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WordWizModule(context: Context) {

  private val appContext: Context = context.applicationContext

  @CheckResult fun provideContext(): Context = appContext

  @CheckResult fun provideMainScheduler(): Scheduler = AndroidSchedulers.mainThread()

  @CheckResult fun provideIoScheduler(): Scheduler = Schedulers.io()

  @CheckResult fun provideComputationScheduler(): Scheduler = Schedulers.computation()
}
