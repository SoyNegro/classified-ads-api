package com.thedarksideofcode.service

import com.thedarksideofcode.DB
import com.thedarksideofcode.model.Classified
import com.thedarksideofcode.paginator.PageRequest
import com.thedarksideofcode.paginator.Paginator
import org.bson.conversions.Bson
import org.litote.kmongo.and
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

    suspend fun deleteById(id: String, owningUSer:String) {
        DB.classifiedsCollection.deleteOne(and(Classified::id eq id, Classified::owningUser eq owningUSer))
    }

    //Get Area
    suspend fun getClassifiedById(id: String, owningUSer: String): Classified? {
        return DB.classifiedsCollection.findOne(and(Classified::id eq id, Classified::owningUser eq owningUSer))
    }

    suspend fun getClassifiedBySubCategory(
        subcategory: String,
        owningUSer:String,
        page: Int = 0,
        size: Int = 100
    ): Paginator<Classified>? {
        return paginate(page, size, and(Classified::subcategory eq subcategory, Classified::owningUser eq owningUSer), descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByState(state: String, owningUSer:String, page: Int = 0, size: Int = 100): Paginator<Classified>? {
        return paginate(page, size, and(Classified::state eq state, Classified::owningUser eq owningUSer), descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByCity(city: String, owningUSer:String, page: Int = 0, size: Int = 100): Paginator<Classified>? {
        return paginate(page, size, and(Classified::city eq city, Classified::owningUser eq owningUSer), descending(Classified::publishedDate))
    }

    suspend fun getClassifiedByPublishedDate(
        publishedDate: LocalDateTime,
        owningUSer:String,
        page: Int = 0,
        size: Int = 100
    ): Paginator<Classified>? {
        return paginate(page, size, and(Classified::publishedDate eq publishedDate, Classified::owningUser eq owningUSer), descending(Classified::publishedDate))
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

    fun validatePaginationParams(size: Int?): Int {
        return when {
            size == null|| size < 0 -> 0
            size > 100 -> 100
            else -> size
        }
    }
}
