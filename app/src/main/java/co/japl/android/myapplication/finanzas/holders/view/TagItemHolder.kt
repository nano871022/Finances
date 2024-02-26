package co.japl.android.myapplication.finanzas.holders.view

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.enums.TagItemEnum
import co.japl.android.myapplication.finanzas.holders.interfaces.IItemHolder

class TagItemHolder (val itemView:View,val action:(TagDTO)->Unit):IItemHolder<TagDTO,TagItemEnum> , RecyclerView.ViewHolder(itemView) {
    var name:TextView?=null
    var btn:ImageView?=null
    var tagDTO:TagDTO?=null
    var linearLayout:LinearLayout?=null
    override fun loadFields() {
        name = itemView.findViewById(R.id.tv_name_til)
        btn = itemView.findViewById(R.id.btn_delete_til)
        linearLayout = itemView.findViewById(R.id.ll_click_til)
        linearLayout?.setOnClickListener { action(tagDTO!!) }
    }

    override fun setFields(values: TagDTO, callback: (TagItemEnum) -> Unit) {
        tagDTO=values
        name?.text = values.name
        btn?.setOnClickListener {
            callback(TagItemEnum.DELETE)
        }
    }
}