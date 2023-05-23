package ge.ted3x.coroutines.flow_vs_livedata

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ge.ted3x.coroutines.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FlowVsLiveDataActivity : AppCompatActivity() {

    private lateinit var vm: FlowVsLiveDataVm
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = FlowVsLiveDataVm()
        setContentView(R.layout.activity_flow_vs_live_data)

        setClickListeners()
    }


    private fun liveDataWithDelay(scope: CoroutineScope) {
        vm.liveData.observeForever {
            // will return both [1,2]
            println("liveData - $it")
        }
        scope.launch {
            // this changes dispatcher to Default dispatcher, since it will freeze Main Thread
            // so we have to use withContext
            delay(5000)
            withContext(Dispatchers.Main) {
                // livedata stores last available value, so it will return 2
                vm.liveData.observeForever {
                    println("liveData[delay] - $it")
                }
            }
        }
    }

    // same as LiveData, StateFlow stores last value and returns it to new subscribers
    private fun stateFlow(scope: CoroutineScope) {
        scope.launch {
            vm.stateFlow.collect {
                println("stateFlow - $it")
            }
        }
        scope.launch {
            delay(1000)
            vm.stateFlow.collect {
                println("stateFlow[delay] - $it")
            }
        }
    }

    private fun sharedFlow(scope: CoroutineScope) {
        scope.launch {
            delay(2000)
            // this sharedFlow has 0 replay, so it won't collect anything since its Hot flow
            // and subscriber became available after emit
            vm.sharedFlow.collect {
                println("sharedFlow - $it")
            }
        }
        scope.launch {
            delay(2000)
            // this sharedFlow has 10 replay, and even though it's subscriber became available after emit
            // it will return last 10 values
            vm.sharedFlowWithReplay.collect {
                println("sharedFlowWithReplay - $it")
            }
        }
    }

    private fun collectVsCollectLatest(scope: CoroutineScope) {
        scope.launch {
            // will collect every value
            vm.flow.collect {
                println("flow[collect] - $it")
            }
        }
        scope.launch {
            // will collect latest value, if new value comes during collecting, current will be cancelled
            vm.flow.collectLatest {
                delay(100)
                println("flow[collectLatest] - $it")
            }
        }
    }

    private fun shareInLazily(scope: CoroutineScope) {
        // Only first subscriber will receive values from Flow
        scope.launch {
            // this will collect every value
            vm.flowWithShareInLazily.collect {
                println("flow[shareIn] - $it")
            }
        }
        scope.launch {
            delay(1000)
            // this won't collect any because [flowWithShareInLazily] is already initialized
            // and Flow's code block is already executed
            vm.flowWithShareInLazily.collect {
                println("flow[shareIn delay] - $it")
            }
        }
    }

    private fun shareInEagerly(scope: CoroutineScope) {
        // None of them will receive any values, because it's started as Eagerly
        // So it does not wait for first subscriber
        scope.launch {
            vm.flowWithShareInEagerly.collect {
                println("flow[shareIn] - $it")
            }
        }
        scope.launch {
            delay(1000)
            vm.flowWithShareInEagerly.collect {
                println("flow[shareIn delay] - $it")
            }
        }
    }

    private fun setClickListeners() {
        findViewById<Button>(R.id.liveDataBtn).setOnClickListener { liveDataWithDelay(scope) }
        findViewById<Button>(R.id.stateFlowBtn).setOnClickListener { stateFlow(scope) }
        findViewById<Button>(R.id.sharedFlowBtn).setOnClickListener { sharedFlow(scope) }
        findViewById<Button>(R.id.collectBtn).setOnClickListener { collectVsCollectLatest(scope) }
        findViewById<Button>(R.id.shareLazilyBtn).setOnClickListener { shareInLazily(scope) }
        findViewById<Button>(R.id.shareEagerlyBtn).setOnClickListener { shareInEagerly(scope) }
    }
}