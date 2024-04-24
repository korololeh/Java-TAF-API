package com.template.qa.cucumber

private const val mockTagPrefix = "@mock:"
private const val delimiter = ":"

fun parseMockTagConfigs(sourceTags: Collection<String>): Map<String, String?> {
    return sourceTags.asSequence()
        .filter { it.startsWith(mockTagPrefix) }
        .map { it.substringAfter(mockTagPrefix) }
        .map { expr ->
            when (expr.contains(delimiter)) {
                true -> expr.substringBefore(delimiter) to expr.substringAfter(delimiter)
                false -> expr to null
            }
        }.toMap().toMutableMap()
}