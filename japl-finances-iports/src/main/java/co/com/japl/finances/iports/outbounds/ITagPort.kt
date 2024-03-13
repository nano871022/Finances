package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.TagDTO

interface ITagPort {

    fun create(dto:TagDTO):Int

    fun delete(codeTag:Int):Boolean

    fun getAll():List<TagDTO>


}