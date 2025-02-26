package domain

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

sealed class RequestState<out T> {
    data object Idle :RequestState<Nothing>()
    data object Loading :RequestState<Nothing>()
    data class Success<T>(val data:T):RequestState<T>()
    data class Error(val message:String):RequestState<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

    private fun getSuccessData() = (this as Success).data
    fun getSuccessDataOrNull():T?{
        return try {
            (this as Success).data
        }catch (e:Exception){
            null
        }
    }
    private fun getErrorMessage() = (this as Error).message
    fun getErrorMessageOrEmpty():String{
        return try {
            (this as Error).message
        }catch (e:Exception){
            ""
        }
    }

    @Composable
    fun DisplayResult(
        onIdle:(@Composable () -> Unit)? = null,
        onLoading:@Composable () -> Unit,
        onSuccess:@Composable (T) -> Unit,
        onError:@Composable (String) -> Unit,
        transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
            fadeIn(tween(300))togetherWith fadeOut(tween(300))
        }
    ) {
        AnimatedContent(
            targetState = this,
            transitionSpec = transitionSpec,
            label = "Animated State"
        ){ state ->
            when(state){
                is Idle -> {
                    onIdle?.invoke()
                }
                is Loading -> {
                    onLoading.invoke()
                }
                is Success -> {
                    onSuccess(state.getSuccessData())
                }
                is Error -> {
                    onError(state.getErrorMessage())
                }
            }
        }
        
    }
 }