package ge.ted3x.coroutines.jobs_and_cancellation

import ge.ted3x.coroutines.BaseExample
import kotlinx.coroutines.*

class CancellationExample : BaseExample() {

    fun nonCooperativeCoroutine() {
        scope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // computation loop, just wastes CPU
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("[nonCooperativeCoroutine] job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("[nonCooperativeCoroutine] main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("[nonCooperativeCoroutine] main: Now I can quit.")
        }
    }

    fun cooperativeCoroutineWithYield() {
        scope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) { // computation loop, just wastes CPU
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("[cooperativeCoroutineWithYield] job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                    yield()
                }
            }
            delay(1300L) // delay a bit
            println("[cooperativeCoroutineWithYield] main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("[cooperativeCoroutineWithYield] main: Now I can quit.")
        }
    }

    fun cooperativeCoroutineWithIsActive() {
        scope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) {
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("[cooperativeCoroutineWithIsActive] job: I'm sleeping ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // delay a bit
            println("[cooperativeCoroutineWithIsActive] main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("[cooperativeCoroutineWithIsActive main: Now I can quit.")
        }
    }

    fun cooperativeCoroutineWithDelay() {
        scope.launch {
            val job = launch {
                var i = 0
                while (i < 5) {
                    delay(500)
                    // print a message twice a second
                    println("[cooperativeCoroutineWithDelay job: I'm sleeping ${i++} ...")
                }
            }
            delay(1300L) // delay a bit
            println("[cooperativeCoroutineWithDelay main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("[cooperativeCoroutineWithDelay] main: Now I can quit.")
        }
    }


    fun cancellingParents() {
        scope.launch {
            val job = scope.launch {
                launch {
                    println("[cancellingParents job1 running")
                    delay(1300)
                    println("[cancellingParents] job1 still running")
                }

                launch {
                    println("[cancellingParents] job2 running")
                    delay(1300)
                    println("[cancellingParents] job2 still running")
                }
            }
            delay(1000)
            job.cancel()
            println("[cancellingParents] Parent cancelled")
        }
    }

    fun cancellingParentsWithDifferentJob() {
        scope.launch {
            val job = scope.launch {
                launch(Job()) {
                    println("[cancellingParentsWithDifferentJob] job1 running")
                    delay(1300)
                    println("[cancellingParentsWithDifferentJob] job1 still running")
                }

                launch {
                    println("[cancellingParentsWithDifferentJob] job2 running")
                    delay(1300)
                    println("[cancellingParentsWithDifferentJob] job2 still running")
                }
            }
            delay(1000)
            job.cancel()
            println("[cancellingParentsWithDifferentJob] Parent cancelled")
        }
    }

    fun runningNonCancelableBlock() {
        scope.launch {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        println("[runningNonCancelableBlock] job: I'm sleeping $i ...")
                        delay(500L)
                    }
                } finally {
                    withContext(NonCancellable) {
                        println("[runningNonCancelableBlock] job: I'm running finally")
                        delay(1000L)
                        println("[runningNonCancelableBlock] job: And I've just delayed for 1 sec because I'm non-cancellable")
                    }
                }
            }
            delay(1300L) // delay a bit
            println("[runningNonCancelableBlock] main: I'm tired of waiting!")
            job.cancelAndJoin() // cancels the job and waits for its completion
            println("[runningNonCancelableBlock] main: Now I can quit.")
        }
    }

    fun cancellingWithTimeout() {
        scope.launch {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("[cancellingWithTimeout] I'm sleeping $i ...")
                    delay(500L)
                }
            }
        }
    }
}