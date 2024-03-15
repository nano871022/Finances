package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.TagDTO
import co.japl.finances.core.usercases.interfaces.creditcard.ITag
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort
import javax.inject.Inject

class TagImpl @Inject constructor(private val tagSvc: ITag) : ITagPort {

    override fun getAll(): List<TagDTO> {
        return tagSvc.getAll()
    }

    override fun get(codeBought: Int): TagDTO? {
        return tagSvc.get(codeBought)
    }

    override fun create(dto: TagDTO): Int {
        return tagSvc.create(dto)
    }

    override fun delete(codeTag: Int): Boolean {
        return tagSvc.delete(codeTag)
    }

    override fun createOrUpdate(codeTag: Int, codeBought: Int): Int {
        return tagSvc.createOrUpdate(codeTag,codeBought)
    }
}