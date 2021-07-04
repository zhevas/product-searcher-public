package ru.kardel.core.model

class Search {
    lateinit var productName: String
    var alternativeNames: MutableList<String> = mutableListOf()
    val searchWords: String = ""
    val exactTerms: MutableList<String> = mutableListOf()
}
