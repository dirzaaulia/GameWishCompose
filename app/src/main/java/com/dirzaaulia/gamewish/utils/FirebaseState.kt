package com.dirzaaulia.gamewish.utils

data class FirebaseState constructor(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = FirebaseState(Status.SUCCESS)
        val IDLE = FirebaseState(Status.IDLE)
        val LOADING = FirebaseState(Status.RUNNING)
        fun error(msg: String?) = FirebaseState(Status.FAILED, msg)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED,
        IDLE,
    }
}