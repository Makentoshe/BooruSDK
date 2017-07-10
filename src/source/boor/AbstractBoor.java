package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Created by Makentoshe on 09.07.2017.
 */
abstract class AbstractBoor {

    public abstract Api getApi();

    public abstract DataType getDataType();

    /**
     * Construct get request.
     *
     * @param itemCount - how many items must be in list.
     * @param request   - request. Contain tags and marks.
     * @param pid       - page number.
     * @return complete get request
     */
    public abstract String getCompleteRequest(int itemCount, String request, int pid);

}
