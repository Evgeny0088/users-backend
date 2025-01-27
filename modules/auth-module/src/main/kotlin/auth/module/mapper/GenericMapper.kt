package auth.module.mapper

interface GenericMapper<Req, Rep, Dto> {

    fun toRepresentation(req: Req): Rep

    fun toEntity(dto: Dto): Rep

    fun toDto(rep: Rep): Dto
}