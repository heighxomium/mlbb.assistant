```kotlin
package com.mlbbassistant.core

import android.content.Context
import android.util.Log
import kotlin.system.exitProcess

/**
 * Last-resort uncaught-exception handler.
 * Logs the crash then delegates to the system default so Android still shows
 * its standard crash dialog / restarts the app.
 */
class GlobalExceptionHandler(
    private val context: Context,
    private val defaultHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            Log.e(TAG, "Uncaught exception on thread ${thread.name}", throwable)
            context.getSharedPreferences(CRASH_PREFS, Context.MODE_PRIVATE)
                .edit()
                .apply {
                    putString(LAST_CRASH_KEY, throwable.message?.take(MAX_CRASH_MESSAGE_LENGTH) ?: UNKNOWN_CRASH_MESSAGE)
                    putLong(LAST_CRASH_TIMESTAMP_KEY, System.currentTimeMillis())
                    apply()
                }
        } catch (inner: Exception) {
            Log.e(TAG, "Exception inside crash handler", inner)
        } finally {
            defaultHandler?.uncaughtException(thread, throwable) ?: exitProcess(EXIT_CODE)
        }
    }

    companion object {
        private const val TAG = "GlobalExceptionHandler"
        private const val CRASH_PREFS = "crash_prefs"
        private const val LAST_CRASH_KEY = "last_crash"
        private const val LAST_CRASH_TIMESTAMP_KEY = "last_crash_ts"
        private const val MAX_CRASH_MESSAGE_LENGTH = 500
        private const val UNKNOWN_CRASH_MESSAGE = "unknown"
        private const val EXIT_CODE = 1
    }
}
```