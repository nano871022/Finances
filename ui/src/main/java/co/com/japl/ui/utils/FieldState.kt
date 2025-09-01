package co.com.japl.ui.utils

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach


class FieldState<T> constructor(
                     private val savedStateHandler: SavedStateHandle,
                     private val key:String,
                     private val initialValue:T,
                     private val listValues:List<T> = emptyList(),
                     private val validator:(T)->Boolean = {false},
                     private val formatter:(String)->T?  = { null },
                     private val onValueChangeCallBack: (T) -> Unit = {}){
    var valueStr:String by mutableStateOf(initialValue.toString())
        private set
    var error = mutableStateOf(false)
        private set
    private var touched:Boolean
        get() = savedStateHandler.get<Boolean>("${key}_TOUCH") ?: false
        set(value){
            savedStateHandler["${key}_TOUCH"] = value
        }
    var list = mutableListOf<T>()
        private set

    val value: StateFlow<T> = savedStateHandler.getStateFlow(key,initialValue)

    init{
        list.addAll(listValues)
        snapshotFlow { value }
            .distinctUntilChanged()
            .onEach{ newValue->
                onValueChangeCallBack.invoke(newValue.value)
            }.onEach { newValue ->
                if(touched){
                    error.value = validator.invoke(newValue.value).not()
                }
            }
    }

    fun onValueChange(newValue:T){
        if(touched.not()){
            touched = true
        }
        savedStateHandler[key] = newValue
        valueStr = newValue?.toString()?:""
        error.value = validator.invoke(newValue).not()
        if(error.value.not()) {
            onValueChangeCallBack.invoke(newValue)
        }
    }

    fun onValueChangeStr(newValue:String){
        Log.d("onValueChangeStr","$newValue")
        if(touched.not()){
            touched = true
        }
        formatter.invoke(newValue)?.let{
            savedStateHandler[key] = it
        }
        valueStr = newValue
        error.value = validator.invoke(value.value).not()
        if(error.value.not()) {
            onValueChangeCallBack.invoke(value.value)
        }
    }

    fun validate():Boolean = validator.invoke(value.value)

    fun reset(initialValue:T){
        savedStateHandler[key] = initialValue
        valueStr = initialValue.toString()
        error.value = false
        touched = false
    }
}

fun <T> ViewModel.initialFieldState(
    savedStateHandler: SavedStateHandle,
    key:String,
    initialValue:T,
    list:List<T> = emptyList(),
    validator:(T)->Boolean = {false},
    formatter:(String)->T?  = { null },
    onValueChangeCallBack: (T) -> Unit = {}
): FieldState<T>{
    return FieldState(savedStateHandler,key,initialValue,list,validator,formatter,onValueChangeCallBack)
}

sealed class FormUIState{
    object Current:FormUIState()
    object Loading:FormUIState()
    object NotData:FormUIState()
    data class Success(val message:String):FormUIState()
    data class Error(val message:String):FormUIState()

}