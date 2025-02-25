package presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.RequestState
import domain.TodoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias MutableTask = MutableState<RequestState<List<TodoTask>>>
typealias Tasks = MutableState<RequestState<List<TodoTask>>>

class HomeViewModel (private val mongoDB: MongoDB): ScreenModel {
    private var _activeTasks: MutableTask = mutableStateOf(RequestState.Idle)
    val activeTasks: Tasks = _activeTasks

    private var _completedTasks:MutableTask = mutableStateOf(RequestState.Idle)
    val completedTasks:Tasks =_completedTasks

    init {
        _activeTasks.value = RequestState.Loading
        _completedTasks.value = RequestState.Loading
        screenModelScope.launch (Dispatchers.Main){
            delay(500)
            mongoDB.readActiveTasks().collectLatest {
                _activeTasks.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main){
            delay(500)
            mongoDB.readCompletedTasks().collectLatest {
                _completedTasks.value = it
            }
        }
    }
}