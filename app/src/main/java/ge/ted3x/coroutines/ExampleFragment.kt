package ge.ted3x.coroutines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ExampleFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            flow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    println(it)
                }
        }
    }
}

val flow = flow<Int> { }