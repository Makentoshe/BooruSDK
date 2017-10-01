package source.еnum;

/**
 * Describes document content, which we will have to parse.
 */
public enum Format {
    XML, //basic content - all post data is storing is an attributes
    JSON, //for each data seg new tag
    UNDEFINED
}
