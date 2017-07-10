import engine.parser.BasicPostParser;
import source.boor.Gelbooru;

/**
 * Created by Makentoshe on 10.07.2017.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        BasicPostParser parser = new BasicPostParser();
        parser.startParse(Gelbooru.get().getCompleteRequest(2, "hatsune_miku",0));

        System.out.println(parser.getResult().size());
    }
}
