package co.com.japl.module.creditcard.views.bought.forms

import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.inbounds.creditcard.ITagPort

class FakeTagPort : ITagPort {
    override fun getAll(): List<TagDTO> {
        return emptyList()
    }

    override fun get(id: Int): TagDTO? {
        return null
    }

    override fun create(tag: TagDTO): Int {
        return 1
    }

    override fun update(tag: TagDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun createOrUpdate(idTag: Int, idBought: Int): Boolean {
        return true
    }
}
