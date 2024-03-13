package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.TagDTO


interface ITagQuoteCreditCardPort {

    fun getTags(id:Int):List<TagDTO>

    fun create(codeTag:Int,codeBought:Int):Int

    fun update(codeTag:Int,codeBought: Int):Boolean

    fun delete(code:Int):Boolean
}