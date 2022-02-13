package com.sam.gogoeat.api.resp.base

data class State<out T>(val status: Status, val data: T?, val message: String?, val nextPageToken: String? = null) {
    companion object {

        fun <T> success(data: T?): State<T> {
            return State(Status.SUCCESS, data, null)
        }

        fun <T> success(data: T?, nextPageToken: String?): State<T> {
            return State(Status.SUCCESS, data, null, nextPageToken)
        }

        fun <T> error(msg: String?): State<T> {
            return State(Status.ERROR, null, msg)
        }

        fun <T> loading(data: T?): State<T> {
            return State(Status.LOADING, data, null)
        }

        fun <T> nothing(): State<T> {
            return State(Status.NOTHING, null, null)
        }

    }

    override fun toString(): String {
        return "Resource{status: ${status.name}}, data: ${data}, message: ${message}"
    }

    fun isSuccess(): Boolean = status == Status.SUCCESS
    fun isFailed(): Boolean = status == Status.ERROR
    fun isFinished(): Boolean = status != Status.LOADING
    fun isLoading(): Boolean = status == Status.LOADING
    fun isNothing(): Boolean = status == Status.NOTHING
    fun hasNextPage(): Boolean = status == Status.SUCCESS && !nextPageToken.isNullOrEmpty()
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    NOTHING
}