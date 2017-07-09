package source.boor;

import source.еnum.Api;

/**
 * Singleton.
 * Storage data about Behoimi API and method for getting request
 */
public class Behoimi extends AbstractBoor {

    private static final Behoimi instance = new Behoimi();

    private final Api api = Api.ADVANCED;



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
