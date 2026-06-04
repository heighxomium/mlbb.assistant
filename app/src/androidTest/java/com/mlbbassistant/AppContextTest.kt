```kotlin
package com.mlbbassistant

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented smoke test — verifies the correct package name is resolved
 * and the app context is accessible on a real device / emulator.
 */
@RunWith(AndroidJUnit4::class)
class AppContextTest {

    @Test
    fun useAppContext() {
        val appContext: Context = ApplicationProvider.getApplicationContext()
        assertEquals("com.mlbbassistant", appContext.packageName)
    }
}
```