package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Created by Makentoshe on 09.07.2017.
 */
abstract class AbstractBoor {

    public abstract Api getApi();

    public abstract Format getFormat();

    /**
     * Construct standard get request.
     *
     * @param itemCount - how many items must be in list.
     * @param request   - request. Contain tags and marks.
     * @param pid       - page number.
     * @param format    - result format(XML or JSON).
     * @return complete get request
     */
    public String getCompleteRequest(int itemCount, String request, int pid, Format format){
        //in some situations this method must be override: page in some servers also has name "pid"
        return  getCustomRequest("limit=" + itemCount + "&tags=" + request + "&page=" + pid, format);
    }

    //when the format is not specified explicitly
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return getCompleteRequest(itemCount, request, pid, getFormat());
    }

    /**
     * Construct custom request.
     *
     * @param request - request.
     * @param format  - result format(XML or JSON).
     * @return complete get request
     */
    public abstract String getCustomRequest(String request, Format format);

    //when the format is not specified explicitly
    public String getCustomRequest(String request) {
        return getCustomRequest(request, getFormat());
    }
}
