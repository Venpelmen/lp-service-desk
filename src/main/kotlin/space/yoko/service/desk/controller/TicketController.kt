package space.yoko.service.desk.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import space.yoko.service.desk.controller.exception.TicketNotFound
import space.yoko.service.desk.data.dto.TicketDto
import space.yoko.service.desk.service.TicketService
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("/tickets")
class TicketController(val ticketService: TicketService) {


    @PutMapping("/{id}")
    fun update(@PathVariable @NotNull id: UUID, @RequestBody @Valid dto: TicketDto): TicketDto {
        return ticketService.update(id, dto) ?: throw TicketNotFound()
    }

    @GetMapping
    fun getAll(pageable: Pageable): Page<TicketDto> {
        return ticketService.getAll(pageable)
    }


    @PostMapping
    fun create(@RequestBody @Valid dto: TicketDto): TicketDto {
        return ticketService.create(dto)
    }


    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): TicketDto {
        return ticketService.findById(id) ?: throw TicketNotFound()
    }


    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID): UUID {
        ticketService.delete(id)
        return id
    }

    @GetMapping("/search")
    @ResponseBody
    fun search(@RequestParam(value = "search") searchInfo: String): List<TicketDto> {
        return ticketService.search(searchInfo)
    }


}
