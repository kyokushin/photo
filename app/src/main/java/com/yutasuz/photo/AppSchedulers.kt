package com.yutasuz.photo

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class AppSchedulers: BaseSchedulers {
    override val io = Schedulers.io()
    override val main = AndroidSchedulers.mainThread()
}