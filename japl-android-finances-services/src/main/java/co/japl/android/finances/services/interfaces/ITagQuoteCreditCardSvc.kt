package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.TagDTO
import co.japl.android.finances.services.dto.TagsQuoteCreditCardDTO

interface ITagQuoteCreditCardSvc : SaveSvc<TagsQuoteCreditCardDTO>{
    fun getTags(codQuoteCreditCard:Int):List<TagDTO>
}