package nl.torquelink.database.exceptions

sealed class PageableExceptions(msg: String) : Exception(msg) {
    class InvalidPageException(page: Int) : PageableExceptions("Page number: $page is invalid")
    class InvalidSizeException(size: Int) : PageableExceptions("Page size: $size is invalid")
}