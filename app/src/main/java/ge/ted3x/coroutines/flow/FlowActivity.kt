package ge.ted3x.coroutines.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.ted3x.coroutines.databinding.ActivityFlowBinding

class FlowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlowBinding
    private val flowExample = FlowExample()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.collectLatestBtn.setOnClickListener { flowExample.collectLatest() }
        binding.conflateBtn.setOnClickListener { flowExample.conflate() }
        binding.collectBtn.setOnClickListener { flowExample.collect() }
        binding.bufferingBtn.setOnClickListener { flowExample.buffering() }
        binding.distinctBtn.setOnClickListener { flowExample.distinctUntilChanged() }
        binding.cancelingBtn.setOnClickListener { flowExample.cancelling() }
        binding.exampleBtn.setOnClickListener { flowExample.example() }
    }
}