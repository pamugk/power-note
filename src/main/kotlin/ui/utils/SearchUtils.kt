package ui.utils

fun tokenize(searchText: String) =
    searchText.split(spaceNormalizationRegex).filter { it.isNotEmpty() }