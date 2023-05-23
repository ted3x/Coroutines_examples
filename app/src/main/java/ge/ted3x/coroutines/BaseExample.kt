package ge.ted3x.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseExample {

    protected val scope get() = CoroutineScope(Dispatchers.Default)
}