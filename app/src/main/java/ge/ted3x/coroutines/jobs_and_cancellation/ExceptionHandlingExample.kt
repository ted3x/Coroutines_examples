package ge.ted3x.coroutines.jobs_and_cancellation

import ge.ted3x.coroutines.BaseExample
import kotlinx.coroutines.*

class ExceptionHandlingExample : BaseExample() {

    fun handlingExceptionInsideChildCoroutine() {
        scope.launch {
            launch {
                println("[handlingExceptionInsideChildCoroutine] job1 running")
                delay(1300)
                println("[handlingExceptionInsideChildCoroutine] job1 still running")
            }

            launch {
                try {
                    println("[handlingExceptionInsideChildCoroutine] job2 running")
                    delay(1000)
                    throw RuntimeException("")
                } catch (e: Exception) {
                    println("[handlingExceptionInsideChildCoroutine] - $e caught")
                }
            }
        }
    }

    fun handlingExceptionInParentCoroutine() {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            println("[handlingExceptionInParentCoroutine] - $e caught")
        }

        scope.launch(exceptionHandler) {
            launch {
                println("[handlingExceptionInParentCoroutine] job1 running")
                delay(1300)
                println("[handlingExceptionInParentCoroutine] job1 still running")
            }

            launch {
                println("[handlingExceptionInParentCoroutine] job2 running")
                delay(1000)
                throw RuntimeException("")
            }
        }
    }

    fun supervisorJob() {
        runBlocking {
            // supervisorScope can be also used instead of this
            scope.launch(SupervisorJob()) {
                launch(Job()) {
                    println("[supervisorJob] job1 running")
                    delay(1300)
                    println("[supervisorJob] job1 still running")
                }

                launch {
                    try {
                        println("[supervisorJob] job2 running")
                        delay(1000)
                        throw RuntimeException()
                    }catch (e: Exception) {
                        println("[supervisorJob] - $e caught")
                    }
                }
            }
        }
    }
}