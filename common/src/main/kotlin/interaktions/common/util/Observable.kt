package interaktions.common.util

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.properties.Delegates

/**
 * AN observable [value] where you can await for an update on [value] by using [awaitChange]
 */
public class Observable<T>(value: T) {
    public var value: T by Delegates.observable(value) { _, _, new ->
        listeners.forEach { it.resume(new) }
    }

    private val listeners = mutableListOf<Continuation<T>>()

    public suspend fun awaitChange(): T {
        return suspendCancellableCoroutine {
            listeners.add(it)
        }
    }
}
