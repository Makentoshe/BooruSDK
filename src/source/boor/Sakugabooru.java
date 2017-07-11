package source.boor;

import source.еnum.Api;
import source.еnum.Format;

/**
 * Created by Бонч on 11.07.2017.
 */
public class Sakugabooru extends AbstractBoor {

    private final static Sakugabooru instance = new Sakugabooru();

    public static Sakugabooru get(){
        return instance;
    }

    private final Api api = Api.ADVANCED;

    private Format format = Format.XML;

    private final String LINK = "https://sakugabooru.com/post/index.xml?";

    public void setFormat(Format format){
        this.format = format;
    }

    @Override
    public Api getApi() {
        return api;
    }

    public Format getFormat() {
        return format;
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
