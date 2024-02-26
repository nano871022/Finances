package co.japl.android.myapplication.finanzas.controller

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.ITagSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.TagsImpl
import co.japl.android.myapplication.finanzas.holders.AmortizationGeneralDialogHolder
import co.japl.android.myapplication.finanzas.holders.TagDialogHolder

class TagsDialog (context:Context, private val tag:TagDTO, val inflater: LayoutInflater,val action:(TagDTO)->Unit): Dialog(context) {
    private var holder:TagDialogHolder? = null
    private var tagSvc:ITagSvc? = TagsImpl(ConnectDB(context))
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.tag_dialog, null)
        val tags = tagSvc?.getAll()
        holder = TagDialogHolder(view,this){
            action(it)
            cancel()
        }
        holder?.let {
                it.setFields{
                    holder?.downLoadFields()?.let { tagDto->
                        try {
                            val id = tagSvc?.save(tagDto)
                            val tags = tagDto.copy(id = id?.toInt()!!)
                            action(tags)
                            cancel()
                        }catch(e:Exception){
                            Toast.makeText(context,"ERROR:: ${e.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                it.loadFields (tag)
            it.loadRecycler(tags?.toMutableList()!!)
        }
        setContentView(view)
    }


}