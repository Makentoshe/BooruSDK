package source.boor;

import source.еnum.Api;

/**
 * Created by Makentoshe on 09.07.2017.
 */
public class Danbooru extends AbstractBoor {

    private static final Danbooru instance = new Danbooru();

    private static final Api api = Api.ADVANCED;



    public static Danbooru get() {
        return instance;
    }

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://danbooru.donmai.us/posts.xml?" +
                "tags="+ request +"&limit="+itemCount+"&page=" + pid;
    }
}
