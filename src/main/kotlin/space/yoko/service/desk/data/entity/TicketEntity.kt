package space.yoko.service.desk.data.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import space.yoko.service.desk.data.Status
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ticket")
@EntityListeners(AuditingEntityListener::class)
class TicketEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(nullable = false)
    var id: UUID?,
    @Column(updatable = false)
    @CreatedDate var createdAt: LocalDateTime? = null,
    @LastModifiedDate var updatedAt: LocalDateTime? = null,
    @CreatedBy
    var createdBy: UUID? = null,
    val serviceDate: LocalDateTime,
    val serviceType: UUID,
    @Enumerated(EnumType.STRING)
    val status: Status
)