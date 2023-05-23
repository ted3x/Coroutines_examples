package ge.ted3x.coroutines.mutex

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

class MutexCheck {

    private val mutex = Mutex()
    private val atomicCounter = AtomicInteger()
    private var counterWithMutex = 0
    private var counterNoMutex = 0

    suspend fun getResults(): Triple<Int, Int, Int> {

        val job1Atomic = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenAtomic()
            }
        }

        val job2Atomic = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenAtomic()
            }
        }

        val job1NoMutex = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenNoMutex()
            }
        }

        val job2NoMutex = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenNoMutex()
            }
        }

        val job3WithMutex = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenWithMutex()
            }
        }

        val job4WithMutex = CoroutineScope(Default).launch {
            for (i in 1..5000) {
                incrementCounterByTenWithMutex()
            }
        }

        joinAll(
            job1Atomic,
            job2Atomic,
            job1NoMutex,
            job2NoMutex,
            job3WithMutex,
            job4WithMutex
        )

        return Triple(atomicCounter.get(), counterNoMutex, counterWithMutex)
    }

    private suspend fun incrementCounterByTenWithMutex() {
        mutex.withLock {
            for (i in 0 until 10) {
                counterWithMutex++
            }
        }
    }

    private fun incrementCounterByTenNoMutex() {
        for (i in 0 until 10) {
            counterNoMutex++
        }
    }

    private fun incrementCounterByTenAtomic() {
        for (i in 0 until 10) {
            atomicCounter.incrementAndGet()
        }
    }

}