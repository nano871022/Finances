package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.outbounds.ITagPort
import co.com.japl.finances.iports.outbounds.ITagQuoteCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.ITag
import javax.inject.Inject

class Tag @Inject constructor(private val tagSvc:ITagPort, private val tagBoughtSvc:ITagQuoteCreditCardPort ): ITag{
    override fun create(dto: TagDTO): Int {
        return tagSvc.create(dto)
    }

    override fun delete(codeTag: Int): Boolean {
        return tagSvc.delete(codeTag)
    }

    override fun getAll(): List<TagDTO> {
        return tagSvc.getAll()
    }

    override fun get(codeBought: Int): TagDTO? {
        return tagBoughtSvc.getTags(codeBought).firstOrNull()
    }

    override fun createOrUpdate(codeTag: Int, codeBought: Int): Int =
        get(codeBought)?.let {
             tagBoughtSvc.update(codeTag,codeBought)?.takeIf { it }?.let { 1 }?:0
        }?:tagBoughtSvc?.let{it.create(codeTag,codeBought)}?:0

}