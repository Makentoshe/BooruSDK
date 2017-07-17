package source.boor;

import source.еnum.Api;
import source.еnum.Format;


public class Sakugabooru extends AbstractBoorAdvanced {

    private final static Sakugabooru instance = new Sakugabooru();

    public static Sakugabooru get(){
        return instance;
    }

    private Format format = Format.JSON;

    public void setFormat(Format format){
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }




    @Override
    public String getCustomRequest(String request) {
        return "https://sakugabooru.com/" + request;
    }

//    @Override
//    public String getCompleteRequest(int itemCount, String request, int pid) {
//        return getCustomRequest("limit=" + itemCount + "&tag=" + request + "&page=" + pid, format);
//    }
//
//    //override, because request need "tag". Not default "tags".
//    public String getCompleteRequest(int itemCount, String request, int pid, Format format){
//        //in some situations this method must be override: page in some servers also has name "pid"
//        return  getCustomRequest("limit=" + itemCount + "&tag=" + request + "&page=" + pid, format);
//    }
}
