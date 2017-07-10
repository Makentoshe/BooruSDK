package source.еnum;

/**
 * Describes document content, which we will have to parse
 */
public enum DataType {
    XML_BASIC, //basic content - all post data is storing is an attributes
    XML_ADVANCED, //for each data seg new tag
    JSON //JSON
}
