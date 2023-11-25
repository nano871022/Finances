package co.japl.android.finances.services.core.mapper


object TagMapper {
    fun mapper(tag: co.japl.android.finances.services.dto.TagDTO):co.japl.finances.core.dto.TagDTO {
        return co.japl.finances.core.dto.TagDTO(
            tag.id,
            tag.create,
            tag.name,
            tag.active
        )

    }
}