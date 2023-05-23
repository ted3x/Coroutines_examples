package ge.ted3x.coroutines.scope_and_context

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ge.ted3x.coroutines.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ScopeAndContextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scope_and_context)
        findViewById<LinearLayout>(R.id.scopeRoot).setOnClickListener { DispatcherUncofined() }
    }

    private fun DispatcherUncofined() {
        val scope = CoroutineScope(Dispatchers.Unconfined)
        scope.launch {
            setResultText("testSuspend - enter [${Thread.currentThread().name}]")
            testSuspend()
            setResultText("testSuspend - done [${Thread.currentThread().name}]")
        }
    }

    private suspend fun testSuspend() {
        delay(1000)
    }

    private suspend fun setResultText(text: Any) {
        withContext(Dispatchers.Main) { findViewById<TextView>(R.id.resultTv).text = text.toString() }
    }
}