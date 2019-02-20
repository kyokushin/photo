package com.yutasuz.photo.screen.top

interface TopContract {

    interface View {
        var presenter: Presenter
    }

    interface Presenter {
        val view: View
        val repository: Repository
    }

    interface Repository {

    }
}