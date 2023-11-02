package com.example.demo.util

object PaginateUtil {

    fun <T> paginationResult(
        elements: MutableList<T>,
        page: Int,
        pageSize: Int
    ): List<T> {
        val listOfAuthorList: MutableList<List<T>> = mutableListOf()
        var maxSize = pageSize

        while (elements.isNotEmpty()) {
            if (maxSize > elements.size) {
                maxSize = elements.size
            }
            val sliceOfList = elements.slice(0 until maxSize)

            listOfAuthorList.add(sliceOfList)
            elements.removeAll(sliceOfList)
        }

        if (page >= listOfAuthorList.size)
            return emptyList()

        return listOfAuthorList[page]
    }


}