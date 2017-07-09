package source.boor;

import source.еnum.Api;

/**
 * Singleton.
 * Storage data about E621 API and method for getting request
 */
public class E621 extends AbstractBoor {

    private static final E621 instance = new E621();

    public static E621 get(){
        return instance;
    }

    private final Api api = Api.ADVANCED;

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://e621.net/post/index.xml?" +
                "limit="+itemCount+"&tags="+request+"&page=" + pid;
    }
}
