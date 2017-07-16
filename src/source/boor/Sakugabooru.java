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

    private Format format = Format.JSON;


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
    public String getCustomRequest(String request, Format format) {
        return "https://sakugabooru.com/post/index." + format.toString().toLowerCase() + "?" + request;
    }

    @Override
    public String getCompleteRequest(int itemCount, String request, int pid) {
        return getCustomRequest("limit=" + itemCount + "&tag=" + request + "&page=" + pid, format);
    }

    //override, because request need "tag". Not default "tags".
    public String getCompleteRequest(int itemCount, String request, int pid, Format format){
        //in some situations this method must be override: page in some servers also has name "pid"
        return  getCustomRequest("limit=" + itemCount + "&tag=" + request + "&page=" + pid, format);
    }
}
