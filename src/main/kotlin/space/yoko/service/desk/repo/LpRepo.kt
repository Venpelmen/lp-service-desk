package space.yoko.service.desk.repo;


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import space.yoko.service.desk.data.entity.TicketEntity

import java.util.*

@NoRepositoryBean
interface LpRepo<T> : JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
}