package space.yoko.service.desk.data.spec

import org.springframework.data.jpa.domain.Specification
import space.yoko.service.desk.data.dto.SearchCriteriaDto
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class BaseSpec<T>(private val criteria: SearchCriteriaDto?) : Specification<T> {

    override fun toPredicate(
        root: Root<T>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): Predicate? {
        if (criteria == null) {
            return null;
        }
        if (criteria.operation.equals(">", ignoreCase = true)) {
            return builder.greaterThanOrEqualTo(
                root.get(criteria.key), criteria.value.toString()
            )
        } else if (criteria.operation.equals("<", ignoreCase = true)) {
            return builder.lessThanOrEqualTo(
                root.get(criteria.key), criteria.value.toString()
            )
        } else if (criteria.operation.equals("in", ignoreCase = true)) {
            return query.where(root.get<Boolean?>(criteria.key).`in`(criteria.value)).restriction

        } else if (criteria.operation.equals(":", ignoreCase = true)) {
            return if (root.get<Any>(criteria.key).javaType == String::class.java) {
                builder.like(
                    builder.lower(
                        root.get(criteria.key)
                    ), "%" + criteria.value.toString().lowercase(Locale.getDefault()) + "%"
                )
            } else {
                builder.equal(root.get<Any>(criteria.key), criteria.value)
            }
        }
        return null
    }
}