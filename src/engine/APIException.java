/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package engine;

public class APIException extends BooruEngineException {

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
