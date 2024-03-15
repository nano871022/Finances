package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.TagMapper
import co.japl.android.finances.services.interfaces.ITagQuoteCreditCardSvc
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.com.japl.finances.iports.dtos.TagDTO
import co.japl.android.finances.services.dto.TagsQuoteCreditCardDTO
import java.time.LocalDate
import javax.inject.Inject

class TagQuoteCreditCardImpl @Inject constructor(private val tagQuoteCreditCardSvc:ITagQuoteCreditCardSvc):ITagQuoteCreditCardPort {
    override fun getTags(id: Int): List<TagDTO> {
        return tagQuoteCreditCardSvc.getTags(id).map(TagMapper::mapper)
    }

    override fun create(codeTag: Int, codeBought: Int): Int {
        val dto = TagsQuoteCreditCardDTO(
            id=0,
            create= LocalDate.now(),
         codQuote=codeBought,
        codTag=codeTag,
         active=true )
        return tagQuoteCreditCardSvc.save(dto).toInt()
    }

    override fun update(codeTag: Int, codeBought: Int): Boolean =
        tagQuoteCreditCardSvc.getAll()?.filter {
            it.codQuote == codeBought
        }?.first()?.let {
            tagQuoteCreditCardSvc.save(it.copy(codTag = codeTag))>0
        }?:false


    override fun delete(code: Int): Boolean {
        return tagQuoteCreditCardSvc.delete(code)
    }

}