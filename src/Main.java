import source.boor.Behoimi;
import source.еnum.Format;

/**
 * TODO: дописать тесты для хмл формата
 */
public class Main {

    public static void main(String[] args) throws Exception{
        System.out.println(Behoimi.get().getCompleteRequest(2, "sas", 0, Format.JSON));

        System.out.println(Behoimi.get().getCompleteRequest(2, "sas", 0, Format.XML));

        Behoimi.get().setFormat(Format.JSON);
        System.out.println(Behoimi.get().getCompleteRequest(2, "sas", 0));
    }
}
