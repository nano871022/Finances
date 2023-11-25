package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.TagMapper
import co.japl.android.finances.services.interfaces.ITagQuoteCreditCardSvc
import co.japl.finances.core.adapters.outbound.interfaces.ITagQuoteCreditCardPort
import co.japl.finances.core.dto.TagDTO
import javax.inject.Inject

class TagQuoteCreditCardImpl @Inject constructor(private val tagQuoteCreditCardSvc:ITagQuoteCreditCardSvc):ITagQuoteCreditCardPort {
    override fun getTags(id: Int): List<TagDTO> {
        return tagQuoteCreditCardSvc.getTags(id).map(TagMapper::mapper)
    }

}