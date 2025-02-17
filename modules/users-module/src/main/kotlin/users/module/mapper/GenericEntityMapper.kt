package users.module.mapper

interface GenericEntityMapper<Entity,  Dto> {

    fun toDto(entity: Entity): Dto

    fun toEntity(dto: Dto): Entity

}