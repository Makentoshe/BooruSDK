package source.boor;

import source.еnum.Api;

/**
 * Singleton.
 * Storage data about Rule34 API and method for getting request
 */
public class Rule34 extends BoorAbstract {

    private static final Rule34 instance = new Rule34();

    private static final Api api = Api.BASICS;

    public static Rule34 get() {
        return instance;
    }

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://rule34.xxx/index.php?page=dapi&s=post&q=index&" +
                "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }
}
