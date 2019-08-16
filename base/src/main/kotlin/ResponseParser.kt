interface ResponseParser<Result> {
    fun parse(response: Response): Result
}