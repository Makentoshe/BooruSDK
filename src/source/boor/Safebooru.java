package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Singleton.
 * Storage data about Safebooru API and method for getting request
 */
public class Safebooru extends AbstractBoor {

    private static final Safebooru instance = new Safebooru();

    public static Safebooru get() {
        return instance;
    }

    private final String LINK = "https://safebooru.org/index.php?page=dapi&s=post&q=index&";

    private final Api api = Api.BASICS;

    private final DataType dataType = DataType.XML_BASIC;

    public Api getApi() {
        return api;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }


    public String getCompleteRequest(int itemCount, String request, int pid) {
        return LINK + "limit=" + itemCount + "&tags=" + request + "&pid=" + pid;
    }

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}
