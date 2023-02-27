package space.yoko.service.desk.data.spec


import org.springframework.data.jpa.domain.Specification
import space.yoko.service.desk.data.dto.SearchCriteriaDto
import java.util.*
import java.util.stream.Collectors

class BaseSpecificationBuilder<T> {
    private val params: MutableList<SearchCriteriaDto> = ArrayList()


    fun with(key: String?, operation: String?, value: Any): BaseSpecificationBuilder<T> {
        var value = value
        if (value.toString().length == 36) {
            value = UUID.fromString(value.toString())
        }
        params.add(SearchCriteriaDto(key!!, operation!!, value))
        return this
    }

    fun build(): Specification<T>? {
        if (params.size == 0) {
            return null
        }
        val specs: List<Specification<T>> =
            params.stream().map { searchCriteriaDto: SearchCriteriaDto -> BaseSpec<T>(searchCriteriaDto) }
                .collect(Collectors.toList())
        var result: Specification<T> = specs[0]
        for (i in 1 until params.size) {
            result = Specification.where(result).and(specs[i])
        }

        return result
    }

}
