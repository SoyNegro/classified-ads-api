package com.thedarksideofcode.paginator

data class PageRequest(val documentCount:Long, val pageCount:Long, val nextPage:Int?, val previousPage:Int?)

data class Paginator<T>(val result:List<T>, val pageRequest: PageRequest)