package space.yoko.service.desk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ServiceDeskApp()

fun main(args: Array<String>) {
    runApplication<ServiceDeskApp>(*args)
}
