package source.boor;

import source.еnum.Api;

/**
 * Created by Makentoshe on 09.07.2017.
 */
public class Behoimi extends AbstractBoor {

    private static final Behoimi instance = new Behoimi();

    private static final Api api = Api.ADVANCED;



    public static Behoimi get(){
        return instance;
    }

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "http://behoimi.org/post/index.xml?" +
                "limit="+itemCount+"&tags="+request+"&page=" + pid;
    }
}
