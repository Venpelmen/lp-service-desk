package space.yoko.service.desk.controller.filter

import com.auth0.jwt.JWT
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


class JwtFilter : GenericFilterBean() {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val httpServletRequest = servletRequest as HttpServletRequest
        val jwt = getTokenFromHttpRequest(httpServletRequest)
        if (jwt != null) {
            val authentication: Authentication? = getAuthentication(jwt)
            if (authentication != null) {
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getTokenFromHttpRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        MDC.put("bearer", bearerToken)
        return if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    /**
     * Parse jwt token and return [Authentication]
     * @param token token encoded in base64
     * @return [Authentication] or null if token is not valid
     */
    private fun getAuthentication(token: String): Authentication? {
        return try {
            val jwt = JWT.decode(token)
            val claims = jwt.claims
            val authorities: MutableList<GrantedAuthority> = if (claims["roles"] != null) {
                claims["roles"]!!.asList(
                    String::class.java
                ).stream().map<GrantedAuthority?> { SimpleGrantedAuthority(it) }.collect(Collectors.toList())
            } else {
                ArrayList()
            }
            val principal = User(claims["sub"]!!.asString(), "", authorities)

            return PreAuthenticatedAuthenticationToken(principal, token, authorities)
        } catch (e: Exception) {
            log.error("Authentication error : ", e)
            null
        }
    }
}
