package ge.ted3x.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ge.ted3x.coroutines.flow.FlowActivity
import ge.ted3x.coroutines.flow_vs_livedata.FlowVsLiveDataActivity
import ge.ted3x.coroutines.jobs_and_cancellation.JobsAndCancellationActivity
import ge.ted3x.coroutines.mutex.MutexActivity
import ge.ted3x.coroutines.scope_and_context.ScopeAndContextActivity

class MainActivity : AppCompatActivity() {

    private val tryEmitExample = TryEmitExample()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.tryEmitBtn).setOnClickListener {
            tryEmitExample.exampleTryEmit()
        }
        findViewById<Button>(R.id.flowsBtn).setOnClickListener {
            startActivity(Intent(this, FlowActivity::class.java))
        }
        findViewById<Button>(R.id.scopeContextBtn).setOnClickListener {
            startActivity(Intent(this, ScopeAndContextActivity::class.java))
        }
        findViewById<Button>(R.id.flowVsLiveDataBtn).setOnClickListener {
            startActivity(Intent(this, FlowVsLiveDataActivity::class.java))
        }
        findViewById<Button>(R.id.mutexBtn).setOnClickListener {
            startActivity(Intent(this, MutexActivity::class.java))
        }
        findViewById<Button>(R.id.jobsBtn).setOnClickListener {
            startActivity(Intent(this, JobsAndCancellationActivity::class.java))
        }
    }
}