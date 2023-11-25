package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.TagDTO


interface ITagQuoteCreditCardPort {

    fun getTags(id:Int):List<TagDTO>
}