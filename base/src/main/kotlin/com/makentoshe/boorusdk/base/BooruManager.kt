package com.makentoshe.boorusdk.base

interface BooruManager {

    fun posts(
        count: Int, page: Page, tags: Tags, parser: (ByteArray) -> List<ParseResult>
    ): List<ParseResult>

    fun posts(id: Id, parser: (ByteArray) -> ParseResult): ParseResult

    fun search(term: String, parser: (ByteArray) -> Array<String>): Array<String>

    fun comments(postId: Id, parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

    fun comments(parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

    fun tags(id: Id, parser: (ByteArray) -> ParseResult): ParseResult

    fun tags(pattern: String, limit: Int = 100, parser: (ByteArray) -> List<ParseResult>): List<ParseResult>

    fun tags(
        pattern: String,
        limit: Int = 100,
        order: Order,
        orderby: Orderby,
        parser: (ByteArray) -> List<ParseResult>
    ): List<ParseResult>
}