package source.boor;

import source.еnum.Api;

/**
 * Created by Makentoshe on 09.07.2017.
 */
abstract class BoorAbstract {

    public abstract Api getApi();

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
