package space.yoko.service.desk.controller.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.function.Consumer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException


@RestControllerAdvice
class GeneralRestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ValidationException): ResponseEntity<*> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST)
        apiError.message = "Validation error"
        apiError.ex = ex
        ex.errors.forEach(Consumer { error: ValidationError ->
            apiError.addSubError(ApiSubError(error.key + ". " + (error.msg)))
        })
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<*> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST)
        apiError.message = "Validation error"
        apiError.ex = ex
        apiError.addValidationErrors(ex.constraintViolations)
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(Exception::class)
    fun handle(
        ex: Exception?,
        request: HttpServletRequest?, response: HttpServletResponse?
    ): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST)
        apiError.message = ex?.message
        apiError.ex = ex
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<*> {
        val apiError = ApiError(ex.status)
        apiError.message = ex.message
        apiError.ex = ex
        return buildResponseEntity(apiError)
    }

    @ExceptionHandler(TicketNotFound::class)
    fun hanldeNotFound(ex: TicketNotFound): ResponseEntity<*> {
        val apiError = ApiError(HttpStatus.NOT_FOUND)
        apiError.message = ex.message
        apiError.ex = ex
        return buildResponseEntity(apiError)
    }

    public override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex)
        apiError.message = "Validation error"
        apiError.ex = ex
        apiError.addValidationErrors(ex.bindingResult.fieldErrors)
        apiError.addValidationError(ex.bindingResult.globalErrors)
        return buildResponseEntity(apiError, request)
    }

    public override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest,
    ): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach(Consumer { t: MediaType? -> builder.append(t).append(", ") })
        return buildResponseEntity(ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            builder.substring(0, builder.length - 2), ex), request)
    }

    public override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest,
    ): ResponseEntity<Any> {
        val error = ex.parameterName + " parameter is missing"
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, error, ex), request)
    }

    public override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest,
    ): ResponseEntity<Any> {
        val error = "Malformed JSON request"
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, error, ex), request)
    }

    public override fun handleHttpMessageNotWritable(
        ex: HttpMessageNotWritableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest,
    ): ResponseEntity<Any> {
        val error = "Error writing JSON output"
        return buildResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex), request)
    }

    public override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest,
    ): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex)
        apiError.message = String.format("Could not find the %s method for URL %s", ex.httpMethod, ex.requestURL)
        return buildResponseEntity(apiError, request)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
    ): ResponseEntity<Any> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex)
        apiError.message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.name, ex.value, Objects.requireNonNull(ex.requiredType)!!.simpleName)
        return buildResponseEntity(apiError)
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        logger.error(stackTraceToString(apiError.ex))
        return ResponseEntity(apiError, apiError.status!!)
    }

    private fun buildResponseEntity(apiError: ApiError, request: WebRequest): ResponseEntity<Any> {
        logger.error(String.format("Exception class: %s, stackTrace: %s, requestPath: %s",
            apiError.ex!!.javaClass.name,
            stackTraceToString(apiError.ex),
            (request as ServletWebRequest).request.servletPath))
        return ResponseEntity(apiError, apiError.status!!)
    }

    private fun stackTraceToString(e: Throwable?): String {
        val sb = StringBuilder()
        sb.append(e!!.message).append("\n")
        for (element in e.stackTrace) {
            sb.append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }
}