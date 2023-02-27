package space.yoko.service.desk.controller.exception

import java.lang.RuntimeException

class ValidationException(val errors: List<ValidationError>) : RuntimeException()