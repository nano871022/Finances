package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.TagDTO


interface ITagQuoteCreditCardPort {

    fun getTags(id:Int):List<TagDTO>
}