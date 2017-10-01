package source.еnum;

/**
 * Enum for post ratings.
 * <p>
 * Image ratings help organize and search through the various levels of explicit content. So, there are 3 categories:
 *
 * <p>{@code Rating.EXPLICIT:} for pornography — images containing explicit sex,
 * gratuitously exposed genitals, or that are otherwise pornographic.
 *
 * <p>{@code Rating.QUESTIONABLE:} for images that may contain some non-explicit nudity or sexual content,
 * but that aren't quite pornographic.
 *
 * <p>{@code Rating.SAFE:} for everything else. Note that {@code Rating.SAFE}
 * <strong>does not mean safe for work!</strong> and may still include "sexy" images.
 */
public enum Rating {
    SAFE,
    QUESTIONABLE,
    EXPLICIT
}
