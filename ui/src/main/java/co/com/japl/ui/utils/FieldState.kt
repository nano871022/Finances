package co.com.japl.ui.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach


class FieldState<T>( initialValue:T,
                     listValues:List<T> = emptyList(),
                     private val validator:(T)->Boolean = {false},
                     private val formatter:(String)->T?  = { null },
                     private val onValueChangeCallBack: (T) -> Unit = {}){
    var value: T by mutableStateOf<T>(initialValue)
        private set
    var valueStr:String by mutableStateOf(initialValue.toString())
        private set
    var error = mutableStateOf(false)
        private set
    var touched:Boolean by mutableStateOf(false)
        private set
    var list: MutableList<T> = mutableStateListOf<T>()
        private set

    private val _valueFlow = MutableStateFlow(initialValue)
    val valueFlow: StateFlow<T> = _valueFlow.asStateFlow()

    init{
        list.addAll(listValues)
        snapshotFlow { value }
            .distinctUntilChanged()
            .onEach{ newValue->
                _valueFlow.value = newValue
                onValueChangeCallBack.invoke(newValue)
            }.onEach { newValue ->
                if(touched){
                    error.value = validator.invoke(newValue).not()
                }
            }
    }

    fun onValueChange(newValue:T){
        if(touched.not()){
            touched = true
        }
        value = newValue
        valueStr = newValue.toString()
        error.value = validator.invoke(newValue).not()
        if(error.value.not()) {
            onValueChangeCallBack.invoke(newValue)
        }
    }

    fun onValueChangeStr(newValue:String){
        if(touched.not()){
            touched = true
        }
        formatter.invoke(newValue)?.let{
            value = it
        }
        valueStr = newValue
        error.value = validator.invoke(value).not()
        if(error.value.not()) {
            onValueChangeCallBack.invoke(value)
        }
    }

    fun validate():Boolean = validator.invoke(value)

    fun reset(initialValue:T){
        value = initialValue
        valueStr = initialValue.toString()
        error.value = false
        touched = false
        _valueFlow.value = initialValue
    }
}

fun <T> initialFieldState(
    initialValue:T,
    list:List<T> = emptyList(),
    validator:(T)->Boolean = {false},
    formatter:(String)->T?  = { null },
    onValueChangeCallBack: (T) -> Unit = {}
): FieldState<T>{
    return FieldState(initialValue,list,validator,formatter,onValueChangeCallBack)
}

sealed class FormUIState{
    object Current:FormUIState()
    object Loading:FormUIState()
    object NotData:FormUIState()
    data class Success(val message:String):FormUIState()
    data class Error(val message:String):FormUIState()

}