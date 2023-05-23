package ge.ted3x.coroutines.mutex

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ge.ted3x.coroutines.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MutexActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutex)

        val resultsTv = findViewById<TextView>(R.id.resultsTv)

        findViewById<Button>(R.id.launchBtn).setOnClickListener {
            resultsTv.text = ""
            scope.launch {
                repeat(10) {
                    val result = MutexCheck().getResults()
                    withContext(Dispatchers.Main) {
                        resultsTv.text =
                            "${resultsTv.text}\nAtomic - ${result.first} | No Mutex - ${result.second} | Mutex - ${result.third}"
                    }
                }
            }
        }
    }
}