package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort

class FakeTagPort : ITagPort {
    override fun getAll(): List<TagDTO> {
        TODO("Not yet implemented")
    }

    override fun get(codeBought: Int): TagDTO? {
        TODO("Not yet implemented")
    }

    override fun create(dto: TagDTO): Int {
        TODO("Not yet implemented")
    }

    override fun delete(codeTag: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun createOrUpdate(codeTag: Int, codeBought: Int): Int {
        TODO("Not yet implemented")
    }

}
