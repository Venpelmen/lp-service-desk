package space.yoko.service.desk.data.spec

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserUtils {
    companion object {
        fun isAdmin(): Boolean {
            val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
            val isAdmin = false
            return isAdmin
        }

        fun getUserId(): UUID {
            val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
            return UUID.fromString(userDetails.username)
        }
    }
}