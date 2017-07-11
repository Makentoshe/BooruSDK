package source.boor;

import source.еnum.Api;
import source.еnum.DataType;

/**
 * Created by Бонч on 11.07.2017.
 */
public class Sakugabooru extends AbstractBoor {

    private final static Sakugabooru instance = new Sakugabooru();

    public static Sakugabooru get(){
        return instance;
    }

    private final Api api = Api.ADVANCED;

    private final DataType dataType = DataType.XML_BASIC;

    private final String LINK = "https://sakugabooru.com/post/index.xml?";

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
        return LINK + "limit="+itemCount+"&tag="+request+"&page="+pid;
    }

    @Override
    public String getCustomRequest(String request) {
        return LINK + request;
    }
}
