package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Singleton.
 * Storage data about Gelbooru API and method for getting request
 */
public class Gelbooru extends AbstractBoor {

    private static final Gelbooru instance = new Gelbooru();

    public static Gelbooru get() {
        return instance;
    }

    private final Api api = Api.BASICS;

    private final DataType dataType = DataType.XML_BASIC;

    @Override
    public Api getApi() {
        return api;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return "https://gelbooru.com/index.php?page=dapi&s=post&q=index&" +
                "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }
}