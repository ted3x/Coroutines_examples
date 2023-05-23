package ge.ted3x.coroutines.flow_vs_livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FlowVsLiveDataVm : ViewModel() {

    val liveData get() = _liveData as LiveData<Int>
    private val _liveData = MutableLiveData<Int>()

    val stateFlow get() = _stateFlow.asStateFlow()
    private val _stateFlow = MutableStateFlow(0)

    val sharedFlow get() = _sharedFlow.asSharedFlow()
    private val _sharedFlow = MutableSharedFlow<Int>()

    val sharedFlowWithReplay get() = _sharedFlowWithReplay.asSharedFlow()
    private val _sharedFlowWithReplay = MutableSharedFlow<Int>(replay = 10)

    val flow = flow {
        repeat(3) { emit(it) }
    }

    val flowWithShareInEagerly =
        flow.shareIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly)

    val flowWithShareInLazily =
        flow.shareIn(CoroutineScope(Dispatchers.Default), SharingStarted.Lazily)

    init {
        val scope = CoroutineScope(Dispatchers.Default)
        // using viewModelScope is recommended in viewModels
        // Because it's lifecycle is handled by viewModel
        // All jobs running in viewmodelScope are cancelled in onCleared state
        scope.launch {

            _stateFlow.emit(1)
            _liveData.postValue(1)

            repeat(10) {
                _sharedFlow.emit(it)
                _sharedFlowWithReplay.emit(it)
            }

            delay(1000)
            _liveData.postValue(2)
        }
    }
}