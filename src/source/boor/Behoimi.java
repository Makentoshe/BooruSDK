package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Singleton.
 * Storage data about Behoimi API, method for getting request and resolving data type.
 */
public class Behoimi extends AbstractBoor {

    private static final Behoimi instance = new Behoimi();

    private final Api api = Api.ADVANCED;

    private final DataType dataType = DataType.XML;

    private final String LINK = "http://behoimi.org/post/index.xml?";

    public static Behoimi get(){
        return instance;
    }

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

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}
