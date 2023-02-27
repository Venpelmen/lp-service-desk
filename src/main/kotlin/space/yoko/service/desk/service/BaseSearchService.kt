package space.yoko.service.desk.service

import org.springframework.data.jpa.domain.Specification
import space.yoko.service.desk.data.spec.BaseSpecificationBuilder
import java.util.regex.Matcher
import java.util.regex.Pattern


abstract class BaseSearchService<T> {

    fun search(searchInfo: String): Specification<T> {
        val builder = BaseSpecificationBuilder<T>()

        val strings = searchInfo.split(",")

        strings.forEach {
            val pattern: Pattern = Pattern.compile("(\\w+?)(:|<|>)(.*)")
            val matcher: Matcher = pattern.matcher(it)
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3))
            }
        }
        addCheckIfNotAdmin(builder)
        return builder.build()!!
    }

    abstract fun addCheckIfNotAdmin(builder: BaseSpecificationBuilder<T>)

}