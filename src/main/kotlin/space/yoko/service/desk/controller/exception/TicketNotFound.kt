package space.yoko.service.desk.controller.exception

import javax.persistence.EntityNotFoundException

class TicketNotFound : EntityNotFoundException("Requested ticket not found")