package space.yoko.service.desk.service


import org.springframework.stereotype.Service
import space.yoko.service.desk.data.dto.TicketDto
import space.yoko.service.desk.data.entity.TicketEntity
import space.yoko.service.desk.repo.TicketRepo

@Service
class TicketService(repo: TicketRepo, ticketSearchService: TicketSearchService) :
    BaseService<TicketEntity, TicketDto>(repo, ticketSearchService) {



    override fun toDto(t: TicketEntity?): TicketDto? {
        return TicketDto.toDto(t!!)
    }

    override fun toEntity(dto: TicketDto): TicketEntity {
        return TicketDto.toEntity(dto)
    }


}