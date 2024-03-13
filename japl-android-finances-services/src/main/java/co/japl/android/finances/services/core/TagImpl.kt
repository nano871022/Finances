package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.TagDTO
import co.com.japl.finances.iports.outbounds.ITagPort
import co.japl.android.finances.services.core.mapper.TagMapper
import co.japl.android.finances.services.interfaces.ITagSvc
import javax.inject.Inject

class TagImpl @Inject constructor(private val tagSvc:ITagSvc):ITagPort {
    override fun create(dto: TagDTO): Int {
        return tagSvc.save(TagMapper.mapper(dto)).toInt()
    }

    override fun delete(codeTag: Int): Boolean {
        return tagSvc.delete(codeTag)
    }

    override fun getAll(): List<TagDTO> {
        return tagSvc.getAll().map ( TagMapper::mapper )
    }
}