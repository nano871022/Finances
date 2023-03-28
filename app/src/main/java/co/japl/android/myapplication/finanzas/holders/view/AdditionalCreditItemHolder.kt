package co.japl.android.myapplication.finanzas.holders.view

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class AdditionalCreditItemHolder(val view:View): ViewHolder(view) {
    lateinit var name:MaterialTextView
    lateinit var value:MaterialTextView
    lateinit var delete:ImageView
    lateinit var edit:ImageView

    fun loadFields(){
        name = view.findViewById(R.id.name_acil)
        value = view.findViewById(R.id.value_acil)
        delete = view.findViewById(R.id.btn_delete_acil)
        edit = view.findViewById(R.id.btn_edit_acil)
    }

    fun setField(values:AdditionalCreditDTO,actions:OnClickListener){
        name.text = values.name
        value.text = NumbersUtil.COPtoString(values.value)
        delete.setOnClickListener(actions)
        edit.setOnClickListener(actions)
    }
}