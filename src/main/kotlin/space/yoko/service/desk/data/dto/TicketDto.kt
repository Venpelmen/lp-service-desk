package space.yoko.service.desk.data.dto

import space.yoko.service.desk.data.Status
import space.yoko.service.desk.data.entity.TicketEntity
import java.time.LocalDateTime
import java.util.*


data class TicketDto(
    var id: UUID? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var createdBy: UUID? = null,
    val serviceDate: LocalDateTime? = null,
    val serviceType: UUID? = null,
    val status: Status? = null
) {
    companion object {
        fun toDto(it: TicketEntity): TicketDto {
            return TicketDto(
                id = it.id,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                createdBy = it.createdBy,
                serviceDate = it.serviceDate,
                serviceType = it.serviceType,
                status = it.status
            )
        }

        fun toEntity(it: TicketDto): TicketEntity {
            return TicketEntity(
                id = it.id,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                serviceDate = it.serviceDate!!,
                serviceType = it.serviceType!!,
                status = it.status!!
            )
        }
    }

}

