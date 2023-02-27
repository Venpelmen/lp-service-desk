package space.yoko.service.desk.controller.exception

import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import java.util.Locale
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kotlin.Throws
import java.io.IOException
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType

class LowerCaseClassNameResolver : TypeIdResolverBase() {
    override fun idFromValue(value: Any): String {
        return value.javaClass.simpleName.lowercase(Locale.getDefault())
    }

    override fun idFromValueAndType(value: Any, suggestedType: Class<*>?): String {
        return idFromValue(value)
    }

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

    @Throws(IOException::class)
    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        return super.typeFromId(context, id)
    }
}