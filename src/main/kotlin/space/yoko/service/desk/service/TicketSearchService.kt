package space.yoko.service.desk.service

import org.springframework.stereotype.Service
import space.yoko.service.desk.data.entity.TicketEntity
import space.yoko.service.desk.data.spec.BaseSpecificationBuilder
import space.yoko.service.desk.data.spec.UserUtils.Companion.isAdmin

@Service
class TicketSearchService : BaseSearchService<TicketEntity>() {

    override fun addCheckIfNotAdmin(builder: BaseSpecificationBuilder<TicketEntity>) {

    }
}