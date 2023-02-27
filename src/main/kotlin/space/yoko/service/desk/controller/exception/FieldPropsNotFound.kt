package space.yoko.service.desk.controller.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import javax.persistence.EntityNotFoundException

class FieldPropsNotFound : EntityNotFoundException("Field properties not found")