package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.TagDTO

interface ITag {

    fun create(dto:TagDTO):Int

    fun delete(codeTag:Int):Boolean

    fun getAll():List<TagDTO>

    fun get(codeBought:Int):TagDTO

}