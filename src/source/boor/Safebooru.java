package source.boor;

import source.еnum.Api;

/**
 * Singleton.
 * Storage data about Safebooru API and method for getting request
 */
public class Safebooru extends AbstractBoor {

    private static final Safebooru instance = new Safebooru();

    public static Safebooru get() {
        return instance;
    }


    private final Api api = Api.BASICS;

    public Api getApi() {
        return api;
    }


    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://safebooru.org/index.php?page=dapi&s=post&q=index&" +
                "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }
}
