package space.yoko.service.desk.service


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import space.yoko.service.desk.repo.LpRepo
import java.util.*

@Service
abstract class BaseService<T : Any, ST>(
    private val repo: LpRepo<T>,
    private val searchService: BaseSearchService<T>
) {
    fun update(id: UUID, dto: ST): ST? {
        val entity = repo.findByIdOrNull(id)
        return if (entity != null) toDto(repo.save(toEntity(dto))) else null
    }

    fun getAll(pageable: Pageable): Page<ST> {
        return repo.findAll(pageable).map { toDto(it) }
    }

    fun create(dto: ST): ST {
        return toDto(repo.save(toEntity(dto)))!!
    }

    fun findById(id: UUID): ST? {
        return toDto(repo.findByIdOrNull(id))
    }

    fun delete(id: UUID): UUID {
        repo.deleteById(id)
        return id
    }

    fun search(searchInfo: String): List<ST> {
        return repo.findAll(searchService.search(searchInfo)).map { toDto(it)!! }
    }

    abstract fun toDto(t: T?): ST?
    abstract fun toEntity(st: ST): T
}