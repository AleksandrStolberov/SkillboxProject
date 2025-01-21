package com.example.skillcinema.data.model

import com.example.skillcinema.MoviesInfo

class SearchSettings(
    val currentYear: Int
) {

    var keyword: String = ""
        private set
    var type: String = MoviesInfo.ALL
        private set
    var country: Map<Int, String> = MoviesInfo.USA
        private set
    var genre: Map<Int, String> = MoviesInfo.ACTION_MOVIE
        private set
    var yearFrom: Int = currentYear - (3 * 11)
        private set
    var yearTo: Int = currentYear
        private set
    var ratingFrom: Double = 0.0
        private set
    var ratingTo: Double = 10.0
        private set
    var order: String = MoviesInfo.ORDER_BY_YEAR
        private set
    var isShown: Boolean = false
        private set

    fun keyword(keyword: String) = apply { this.keyword = keyword }
    fun type(type: String) = apply { this.type = type }
    fun country(country: Map<Int, String>) = apply { this.country = country }
    fun genre(genre: Map<Int, String>) = apply { this.genre = genre }
    fun yearFrom(yearFrom: Int) = apply { this.yearFrom = yearFrom }
    fun yearTo(yearTo: Int) = apply { this.yearTo = yearTo }
    fun ratingFrom(ratingFrom: Double) = apply { this.ratingFrom = ratingFrom }
    fun ratingTo(ratingTo: Double) = apply { this.ratingTo = ratingTo }
    fun order(order: String) = apply { this.order = order }
    fun isShown(isShown: Boolean) = apply { this.isShown = isShown }

    override fun toString(): String {
        return "Settings($keyword, $type, $yearFrom, $yearTo, $ratingFrom, $ratingTo, $order)"
    }

}