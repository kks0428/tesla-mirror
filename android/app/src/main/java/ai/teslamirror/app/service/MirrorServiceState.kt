package ai.teslamirror.app.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MirrorServiceState {
    private val _status = MutableStateFlow("idle")
    val status: StateFlow<String> = _status.asStateFlow()

    fun update(value: String) {
        _status.value = value
    }
}
