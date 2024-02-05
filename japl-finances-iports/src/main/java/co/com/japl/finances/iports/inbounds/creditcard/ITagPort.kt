package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.TagDTO

interface ITagPort {

    fun getAll():List<TagDTO>

    fun get(codeBought:Int):TagDTO

    fun create(dto:TagDTO):Int

    fun delete(codeTag:Int):Boolean
}