package source.boor;

import source.еnum.Api;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class Yandere extends AbstractBoor {

    private static final Yandere instance = new Yandere();

    public static Yandere get(){
        return instance;
    }

    private final Api api = Api.ADVANCED;

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://yande.re/post.xml?" +
                "limit="+itemCount+"&tags="+request+"&page="+pid;
    }
}
