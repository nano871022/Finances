package co.japl.android.myapplication.finanzas.bussiness.interfaces

import android.view.View
import co.japl.android.myapplication.bussiness.DTO.CalcDTO

interface ITableHolder<T> {

    fun setup(actions: View.OnClickListener?)

    fun setData(creditValue:CalcDTO)

    fun create()

    fun load()

    fun add(name:String,value:Any)

}