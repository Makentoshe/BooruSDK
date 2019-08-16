abstract class ApiManager<Result> {
    abstract fun execute(request: Request, parser: ResponseParser<Result>): Result
}