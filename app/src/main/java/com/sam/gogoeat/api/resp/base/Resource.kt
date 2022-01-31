package com.sam.gogoeat.api.resp.base

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val nextPageToken: String? = null) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> success(data: T?, nextPageToken: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, nextPageToken)
        }

        fun <T> success(data: ArrayList<T>?): Resource<ArrayList<T>> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> unknownFinish(): Resource<T> {
            return Resource(Status.UNKNOWN_FINISH, null, null)
        }


        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> nothing(): Resource<T> {
            return Resource(Status.NOTHING, null, null)
        }
    }

    override fun toString(): String {
        return "Resource{status: ${status.name}}, data: ${data}, message: ${message}"
    }

    fun isSuccess(): Boolean = status == Status.SUCCESS
    fun isFailed(): Boolean = status == Status.ERROR
    fun isFinished(): Boolean = status != Status.LOADING
    fun isLoading(): Boolean = status == Status.LOADING
    fun isUnknownFinished(): Boolean = status == Status.UNKNOWN_FINISH
    fun isNothing(): Boolean = status == Status.NOTHING
    fun hasNextPage(): Boolean = !nextPageToken.isNullOrEmpty()
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    UNKNOWN_FINISH,
    NOTHING
}