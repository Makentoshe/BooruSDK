package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Singleton.
 * Storage data about Rule34 API and method for getting request
 */
public class Rule34 extends AbstractBoor {

    private static final Rule34 instance = new Rule34();

    private static final Api api = Api.BASICS;

    public static Rule34 get() {
        return instance;
    }

    private final Format format = Format.XML;

    private final String LINK = "https://rule34.xxx/index.php?page=dapi&s=post&q=index&";

    @Override
    public Api getApi() {
        return api;
    }

    public Format getFormat() {
        return format;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return  LINK + "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}
