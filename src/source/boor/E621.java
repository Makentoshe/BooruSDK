package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Singleton.
 * Storage data about E621 API and method for getting request
 */
public class E621 extends AbstractBoor {

    private static final E621 instance = new E621();

    public static E621 get(){
        return instance;
    }

    private final DataType dataType = DataType.XML_ADVANCED;

    private final Api api = Api.ADVANCED;

    private final String LINK = "https://e621.net/post/index.xml?";

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
        return LINK + "limit="+itemCount+"&tags="+request+"&page=" + pid;
    }
}
