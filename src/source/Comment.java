package source;

/**
 * Simple class which can describe comment for post from all boors.
 * <p>
 * If you want add more functionality - just extend this class.
 * <p>
 * This class getting as is. That means, that you <strong>can't</strong> modify field after data defined.
 * Of course, when field is undefined you can setup value.
 * If you want change one or more fields - you must create new instance.
 * When data is defined - all setter calls will be ignored.
 * <p>
 * This class have default constructor for boors, and it isn't guaranteed that it will work correctly.
 * <p>
 *
 */
public class Comment {

    private int id = Integer.MIN_VALUE;

    private String created_at = null;

    private int post_id = Integer.MIN_VALUE;

    private int creator_id = Integer.MIN_VALUE;

    private String body = null;

    private String creator_name = null;



}
