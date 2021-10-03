package com.sam.gogoeat.api.resp.base

data class Resource<out T>(val status: Status, val data: T?, val message: ErrorMessage?, val nextPageToken: String? = null) {
    companion object {
        const val ERROR_CODE_603304 = 603304
        const val MOBAPP_TOKEN_EXPIRED = 100013
        const val OPEN_TOKEN_EXPIRED = 200007

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> success(data: T?, nextPageToken: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, nextPageToken)
        }

        fun <T> success(data: ArrayList<T>?): Resource<ArrayList<T>> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: ErrorMessage): Resource<T> {
            return Resource(Status.ERROR, null, msg)
        }

        fun <T> error(code: Int?, msg: String?): Resource<T> {
            val message = ErrorMessage()
            message.code = code ?: ERROR_CODE_603304
            message.message = msg ?: ""
            return Resource(Status.ERROR, null, message)
        }

        fun <T> unknownFinish(): Resource<T> {
            return Resource(Status.UNKNOWN_FINISH, null, null)
        }


        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> nothing(data: T?): Resource<T> {
            return Resource(Status.NOTHING, data, null)
        }
    }

    override fun toString(): String {
        return "Resource{status: ${status.name}}, data: ${data}, message: ${message}"
    }

    fun isTokenExpired(): Boolean = message?.code == MOBAPP_TOKEN_EXPIRED || message?.code == OPEN_TOKEN_EXPIRED
    fun isSuccess(): Boolean = status == Status.SUCCESS
    fun isFailed(): Boolean = status == Status.ERROR
    fun isFinished(): Boolean = status != Status.LOADING
    fun isLoading(): Boolean = status == Status.LOADING
    fun isUnknownFinished(): Boolean = status != Status.UNKNOWN_FINISH
    fun hasNextPage(): Boolean = !nextPageToken.isNullOrEmpty()
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    UNKNOWN_FINISH,
    NOTHING
}