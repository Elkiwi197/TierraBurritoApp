package com.example.tierraburritoapp.data.remote

sealed class NetworkResult<T>(
    var data: T? = null,
    val message: String? = null,
    val code: Int
) {

    class Success<T>(data: T, code: Int) : NetworkResult<T>(data = data, code = code)
    class Error<T>(message: String, data: T? = null, code: Int) :
        NetworkResult<T>(data = data, message = message, code = code)
}

