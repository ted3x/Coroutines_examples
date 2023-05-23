package ge.ted3x.coroutines.jobs_and_cancellation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ge.ted3x.coroutines.R

class JobsAndCancellationActivity : AppCompatActivity() {

    private val cancellationExample = CancellationExample()
    private val exceptionHandlingExample = ExceptionHandlingExample()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_and_cancellation)
        findViewById<Button>(R.id.nonCooperativeBtn).setOnClickListener {
            cancellationExample.nonCooperativeCoroutine()
        }
        findViewById<Button>(R.id.cooperativeYieldBtn).setOnClickListener {
            cancellationExample.cooperativeCoroutineWithYield()
        }
        findViewById<Button>(R.id.cooperativeBtn).setOnClickListener {
            cancellationExample.cooperativeCoroutineWithDelay()
        }
        findViewById<Button>(R.id.cooperativeIsActiveBtn).setOnClickListener {
            cancellationExample.cooperativeCoroutineWithIsActive()
        }
        findViewById<Button>(R.id.parentCancelBtn).setOnClickListener {
            cancellationExample.cancellingParents()
        }
        findViewById<Button>(R.id.childWithDiffJobBtn).setOnClickListener {
            cancellationExample.cancellingParentsWithDifferentJob()
        }
        findViewById<Button>(R.id.cancelBlockBtn).setOnClickListener {
            cancellationExample.runningNonCancelableBlock()
        }
        findViewById<Button>(R.id.withTimeoutBtn).setOnClickListener {
            cancellationExample.cancellingWithTimeout()
        }

        findViewById<Button>(R.id.exceptionChildBtn).setOnClickListener {
            exceptionHandlingExample.handlingExceptionInsideChildCoroutine()
        }
        findViewById<Button>(R.id.exceptionParentBtn).setOnClickListener {
            exceptionHandlingExample.handlingExceptionInParentCoroutine()
        }
        findViewById<Button>(R.id.supervisorBtn).setOnClickListener {
            exceptionHandlingExample.supervisorJob()
        }
    }
}