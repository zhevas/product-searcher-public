package ru.kardel.core.model

data class GooglePages(var items: List<SearchPage> = emptyList())

data class SearchPage(var link: String)
