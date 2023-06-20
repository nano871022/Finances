package co.japl.android.myapplication.finanzas.holders.view

import android.app.AlertDialog
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.enums.MoreOptionsItemsAdditional
import co.japl.android.myapplication.finanzas.putParams.PeriodCreditListParams
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class AdditionalCreditItemHolder(val view:View): ViewHolder(view) {
    lateinit var name:TextView
    lateinit var value: TextView
    lateinit var more:ImageView
    val items = view.resources.getStringArray(R.array.additional_items_options)

    fun loadFields(){
        name = view.findViewById(R.id.name_acil)
        value = view.findViewById(R.id.value_acil)
        more = view.findViewById(R.id.btn_more_acil)
    }

    fun setField(values:AdditionalCreditDTO,callback:(MoreOptionsItemsAdditional)->Unit){
        name.text = values.name
        value.text = NumbersUtil.toString(values.value)

        val builder = AlertDialog.Builder(itemView.context)
        builder.apply {
            setTitle(view.resources.getString(R.string.pick_option))
            setItems(items) { _, which ->
                callback.invoke(MoreOptionsItemsAdditional.values()[which])
            }
        }
        more.setOnClickListener{
            builder.create().show()
        }
    }
}