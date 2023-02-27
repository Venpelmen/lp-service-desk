package space.yoko.service.desk.controller.exception

class ApiValidationError : ApiSubError {
    private var `object`: String
    private var field: String? = null
    private var rejectedValue: Any? = null

    internal constructor(`object`: String, message: String?) : super(message) {
        this.`object` = `object`
    }

    constructor(`object`: String, field: String?, rejectedValue: Any?, message: String?) : super(message) {
        this.`object` = `object`
        this.field = field
        this.rejectedValue = rejectedValue
    }
}