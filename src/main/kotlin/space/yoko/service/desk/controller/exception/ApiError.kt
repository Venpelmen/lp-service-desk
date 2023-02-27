package space.yoko.service.desk.controller.exception

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.LocalDateTime
import javax.validation.ConstraintViolation


@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT,
    use = JsonTypeInfo.Id.CUSTOM,
    property = "error",
    visible = true)
@JsonTypeIdResolver(
    LowerCaseClassNameResolver::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiError(
    var status: HttpStatus? = null,
    @JsonSerialize(using = ApiErrorLocalDateTimeSerializer::class)
    var timestamp: LocalDateTime? = null,
    var message: String? = null,
    var subErrors: MutableList<ApiSubError>? = null,
    @JsonIgnore
    var ex: Throwable? = null,
) {

    init {
        timestamp = LocalDateTime.now()
    }

    constructor(status: HttpStatus?) : this() {
        this.status = status
    }

    constructor(status: HttpStatus?, ex: Throwable?) : this() {
        this.status = status
        message = "Unexpected error"
        this.ex = ex
    }

    constructor(status: HttpStatus?, message: String?, ex: Throwable?) : this() {
        this.status = status
        this.message = message
        this.ex = ex
    }

    fun addSubError(subError: ApiSubError) {
        if (subErrors == null) {
            subErrors = ArrayList()
        }
        subErrors!!.add(subError)
    }

    private fun addValidationError(`object`: String, field: String, rejectedValue: Any, message: String) {
        addSubError(ApiValidationError(`object`, field, rejectedValue, message))
    }

    private fun addValidationError(`object`: String, message: String) {
        addSubError(ApiValidationError(`object`, message))
    }

    private fun addValidationError(fieldError: FieldError) {
        this.addValidationError(
            fieldError.objectName,
            fieldError.field,
            fieldError.rejectedValue ?: "",
            fieldError.defaultMessage ?: "")
    }

    fun addValidationErrors(fieldErrors: List<FieldError>) {
        fieldErrors.forEach { fieldError: FieldError -> this.addValidationError(fieldError) }
    }

    private fun addValidationError(objectError: ObjectError) {
        this.addValidationError(
            objectError.objectName,
            objectError.defaultMessage ?: "")
    }

    fun addValidationError(globalErrors: List<ObjectError>) {
        globalErrors
            .forEach { objectError: ObjectError? ->
                if (objectError != null) {
                    this.addValidationError(objectError)
                }
            }
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private fun addValidationError(cv: ConstraintViolation<*>) {
        this.addValidationError(
            cv.rootBeanClass.simpleName,
            (cv.propertyPath as PathImpl).leafNode.asString(),
            cv.invalidValue,
            cv.message)
    }

    fun addValidationErrors(constraintViolations: Set<ConstraintViolation<*>?>) {
        constraintViolations.forEach { cv: ConstraintViolation<*>? ->
            if (cv != null) {
                this.addValidationError(cv)
            }
        }
    }
}