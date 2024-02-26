package co.japl.android.myapplication.finanzas.holders

import android.app.Dialog
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.adapter.TagAdapter
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecyclerView
import co.japl.android.myapplication.finanzas.holders.validations.*
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class TagDialogHolder (var view:View,val dialog:Dialog,val action:(TagDTO)->Unit): IHolder<TagDTO>, IRecyclerView<TagDTO> {
    var listRV:RecyclerView? = null
    var btnAdd:ImageView? = null
    var btnClose:Button?= null
    var tag:TextInputEditText? = null
    var tagDto:TagDTO? = null

    val validations by lazy {
        arrayOf(
            tag!! set R.string.error_empty `when` { text().isEmpty()}
        )
    }

    override fun setFields(actions: View.OnClickListener?) {
        listRV = view.findViewById(R.id.rv_list_tags)
        btnAdd = view.findViewById(R.id.btn_add_tgd)
        tag = view.findViewById(R.id.et_tag_tgd)
        btnClose = view.findViewById(R.id.btn_close_tgd)
        btnAdd?.setOnClickListener {
            if(validate()) {
                actions?.onClick(it)
            }
        }
        btnClose?.setOnClickListener{dialog.cancel()}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downLoadFields(): TagDTO {
        if(tagDto != null && tagDto?.id!! > 0){
            return tagDto!!
        }
        return TagDTO(0, LocalDate.now(),tag?.text.toString(),true)
    }

    override fun cleanField() {
        tag?.text?.clear()
    }

    override fun validate(): Boolean {
        var validate = false
        validations.firstInvalid{requestFocus()}.notNull { validate= true }
        return validate

    }

    override fun loadFields(values: TagDTO) {
        tag?.setText(values.name)
        tagDto = values
    }

    override fun loadRecycler(data: MutableList<TagDTO>) {
        listRV?.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        listRV?.adapter = TagAdapter(data){action(it)}
    }
}