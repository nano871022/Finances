package co.japl.android.myapplication.holders.view

import android.app.AlertDialog
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAccount
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsInput
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil

class InputItemHolder(var view:View) : RecyclerView.ViewHolder(view) {
    lateinit var date:TextView
    lateinit var name:TextView
    lateinit var value:TextView
    lateinit var more:AppCompatImageView
    val items = view.resources.getStringArray(R.array.input_items_options)

    fun loadFields(){
        date = view.findViewById(R.id.date_input_iil)
        name = view.findViewById(R.id.name_iil)
        value = view.findViewById(R.id.value_iil)
        more = view.findViewById(R.id.btn_more_iil)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFields(values:InputDTO, callback:(MoreOptionsItemsInput)->Unit){
        date.text = DateUtils.localDateToString( values.date)
        name.text = values.name
        value.text = NumbersUtil.toString(values.value)

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { _, index ->
                callback.invoke(MoreOptionsItemsInput.values()[index])
            }
        }
        more.setOnClickListener {
            builder.create().show()
        }
    }
}