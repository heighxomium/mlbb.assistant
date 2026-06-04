```kotlin
package com.mlbbassistant.core

/**
 * A generic wrapper used by the repository layer to surface success, loading,
 * and error states to ViewModels without leaking exceptions.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    val isSuccess: Boolean get() = this is Success<*>
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading
}

/**
 * Convenience helper: runs [block], wraps the result in [Resource.Success], or
 * catches any [Exception] and wraps it in [Resource.Error].
 */
suspend inline fun <T> safeCall(block: suspend () -> T): Resource<T> =
    try {
        Resource.Success(block())
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Unknown error", e)
    }
```