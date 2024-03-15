package co.japl.android.finances.services.core.mapper


object TagMapper {
    fun mapper(tag: co.japl.android.finances.services.dto.TagDTO):co.com.japl.finances.iports.dtos.TagDTO {
        return co.com.japl.finances.iports.dtos.TagDTO(
            tag.id,
            tag.create,
            tag.name,
            tag.active
        )

    }

    fun mapper(tag: co.com.japl.finances.iports.dtos.TagDTO):co.japl.android.finances.services.dto.TagDTO {
        return co.japl.android.finances.services.dto.TagDTO(
            tag.id,
            tag.create,
            tag.name,
            tag.active
        )

    }
}