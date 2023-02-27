package space.yoko.service.desk.data.entity.auditor

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*


@Component
class SecurityAuditorAware : AuditorAware<UUID> {
    override fun getCurrentAuditor(): Optional<UUID> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        return if (authentication == null || !authentication.isAuthenticated) {
            // Optional.empty()
            Optional.of(UUID.randomUUID())
        } else {
            /*    val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
                UUID.fromString(userDetails.username)
                Optional.of(UUID.fromString(userDetails.username))*/
            Optional.of(UUID.randomUUID())
        }
    }
}