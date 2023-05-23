package ge.ted3x.coroutines.flow

import ge.ted3x.coroutines.BaseExample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class FlowExample : BaseExample() {

    val flow = flowOf(1, 2, 3, 4, 5)

    private val flowWithDuplicateValue = flow {
        emitAll(flow)
        emit(5)
    }

    private val flowWithDelay = flow {
        repeat(5) {
            delay(100)
            emit(it)
        }
    }

    private val flowWithException = flow {
        emit(1)
        throw RuntimeException()
    }

    // changing dispatcher with [withContext] is not possible
    // flowOn is used to change Dispatcher
    val flowOnDefaultDispatcher = flow.flowOn(Dispatchers.Default)

    // shareIn is not suspending function
    // because it does not care about first value [default replay = 0]
    val flowToSharedFlow = flow.shareIn(scope, SharingStarted.Lazily)

    fun collectLatest() {
        scope.launch {
            // works similarly as conflate,
            // except it cancels collecting when new values is available
            flow.collectLatest {
                delay(100)
                println("[flow collectLatest] $it")
            }
        }
    }

    fun conflate() {
        scope.launch {
            // conflate is used if we need only recent values, others are skipped
            flow.conflate().collect {
                delay(100)
                println("[flow conflate] $it")
            }
        }
    }

    fun collect() {
        scope.launch {
            flow.collect {
                println("[flow collect] $it")
            }
        }
    }

    fun buffering() {
        scope.launch {
            val time = measureTimeMillis {
                flowWithDelay
                    .buffer()
                    .collect {
                        delay(300)
                    }
            }
            println("Collected with buffer in $time ms")
        }

        scope.launch {
            val time = measureTimeMillis {
                flowWithDelay
                    .collect {
                        delay(300)
                    }
            }
            println("Collected without buffer in $time ms")
        }
    }

    fun distinctUntilChanged() {
        scope.launch {
            flowWithDuplicateValue.distinctUntilChanged().collect {
                println("[flow distinctUntilChanged] $it")
            }
        }
    }

    fun cancelling() {
        scope.launch {
            // cancellable is not needed since flow { } builder
            // checks if coroutine is active on each emitted value
            flow.collect {

                if (it == 2) cancel()
                else println(it)
            }
        }
        scope.launch {
            // cancellable is needed, because it's busy flow
            // there is no suspending point inside that flow
            (1..5).asFlow().cancellable().collect {
                if (it == 2) cancel()
                else println(it)
            }
        }
    }

    fun example() {
        scope.launch {
            flowWithException.onStart {
                println("Before I start collecting, I print this")
            }.onEach {
                println("On each emitted value I print $it")
            }.catch { throwable ->
                // if we will handle throwable before onCompletion
                // it will not receive $throwable
                println("flow caught $throwable")
            }.onCompletion { throwable ->
                if (throwable == null)
                    println("flow completed successfully or throwable was already caught")
                else println("flow failed with $throwable")
            }.launchIn(scope)
        }
    }

    init {
        scope.launch {
            // stateIn is suspending function
            // it immediately starts collecting values
            // and suspends the calling coroutine until the first value is available
            val flowToStateFlow = flow.stateIn(scope)
            // it's same as collecting in scope
            // scope.launch { flowToStateFlow.collect() }
            flowToStateFlow.launchIn(scope)
        }
    }
}