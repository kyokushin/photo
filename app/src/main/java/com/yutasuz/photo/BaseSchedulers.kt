package com.yutasuz.photo

import io.reactivex.Scheduler

interface BaseSchedulers {
    val io: Scheduler
    val main: Scheduler
}