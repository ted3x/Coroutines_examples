package ge.ted3x.coroutines

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TryEmitExample : BaseExample() {

    //    val shared = MutableSharedFlow(
//        replay = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
    private val stateFlow = MutableStateFlow(0)

    private val sharedFlowLatest = MutableSharedFlow<Int>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    private val sharedFlowOldest = MutableSharedFlow<Int>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val sharedFlowSuspending = MutableSharedFlow<Int>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    init {
        scope.launch {
            sharedFlowLatest.collect {
                delay(200)
                println("sharedFlowLatest - $it")
            }
        }
        scope.launch {
            sharedFlowOldest.collect {
                delay(200)
                println("sharedFlowOldest - $it")
            }
        }
        scope.launch {
            sharedFlowSuspending.collect {
                delay(200)
                println("sharedFlowSuspending - $it")
            }
        }
        scope.launch {
            stateFlow.collect {
                delay(200)
                println("stateFlow - $it")
            }
        }
    }

    fun exampleTryEmit() {

        scope.launch {
            repeat(10) {
                val result = sharedFlowLatest.tryEmit(it)
                println("tryEmit[Latest] $it = $result")
            }
            delay(2000)
            repeat(10) {
                val result = sharedFlowOldest.tryEmit(it)
                println("tryEmit[Oldest] $it = $result")
            }
            delay(2000)
            repeat(10) {
                val result = sharedFlowSuspending.tryEmit(it)
                println("tryEmit[Suspending] $it = $result")
            }
            delay(2000)
        }
    }
}