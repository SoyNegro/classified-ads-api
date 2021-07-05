package com.thedarksideofcode.service

import com.thedarksideofcode.DB
import com.thedarksideofcode.model.Classified
import com.thedarksideofcode.paginator.PageRequest
import com.thedarksideofcode.paginator.Paginator
import org.bson.conversions.Bson
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import java.time.LocalDateTime

class ClassifiedService {

    suspend fun save(classified: Classified) {
        DB.classifiedsCollection.save(classified)
    }

    suspend fun update(classified: Classified) {
        DB.classifiedsCollection.updateOneById(classified.id, classified)
    }

    suspend fun deleteById(id: String) {
        DB.classifiedsCollection.deleteOneById(id)
    }

    //Get Area
    suspend fun getClassifiedById(id: String): Classified? {
        return DB.classifiedsCollection.findOneById(id)
    }

    suspend fun getClassifiedBySubCategory(
        subcategory: String,
        page: Int = 0,
        size: Int = 100
    ): Paginator<Classified>? {
        return paginate(page, size, Classified::subcategory eq subcategory, descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByState(state: String, page: Int = 0, size: Int = 100): Paginator<Classified>? {
        return paginate(page, size, Classified::state eq state, descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByCity(city: String, page: Int = 0, size: Int = 100): Paginator<Classified>? {
        return paginate(page, size, Classified::city eq city, descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByPublishedDate(
        publishedDate: LocalDateTime,
        page: Int = 0,
        size: Int = 100
    ): Paginator<Classified>? {
        return paginate(page, size, Classified::publishedDate eq publishedDate, descending(Classified::publishedDate))
    }

    private suspend fun paginate(page: Int, size: Int, filter: Bson, sort: Bson): Paginator<Classified>? {
        val documentsCount = DB.classifiedsCollection.estimatedDocumentCount()
        val totalPages = (documentsCount / size) + 1
        return if (page <= totalPages) {
            val skippedDocuments: Int = page * size
            val result = DB.classifiedsCollection.find(filter).skip(skippedDocuments).limit(size).sort(sort).toList()
            val prev = if (page > 0) page - 1 else null
            val next = if (result.isNotEmpty() && page <= totalPages) page + 1 else null
            val pageRequest = PageRequest(documentsCount, totalPages, next, prev)
            Paginator(result, pageRequest)

        } else null

    }

    fun validateSize(size: Int?): Int {
        return when {
            size == null|| size < 0 -> 0
            size > 100 -> 100
            else -> size
        }
    }
}