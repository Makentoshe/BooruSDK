package test.source;

import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.boor.*;
import source.еnum.Api;
import source.еnum.Format;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;


public class TestHelper {

    private static File getDataFile() {
        return new File(new File("").getAbsolutePath() + "\\src\\Data");
    }

    public static HashMap<String, String> getDataFromBoorAdvanced(AbstractBoor boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(request1);

        JsonParser parser = new JsonParser();

        parser.startParse(connection.getResponse());

        return parser.getResult().get(0);
    }

    public static HashMap<String, String> getPostFromBoor(AbstractBoor boor, int id) throws Exception {
        if (boor.getApi() == Api.BASICS) {

            String request = boor.getPostByIdRequest(id);
            XmlParser parser = new XmlParser().startParse(request);
            return parser.getResult().get(0);

        } else{
            String request = boor.getPostByIdRequest(id, Format.JSON);
            JsonParser parser = new JsonParser()
                    .startParse(new HttpsConnection()
                            .setRequestMethod(Method.GET)
                            .setUserAgent(HttpsConnection.getDefaultUserAgent())
                            .openConnection(request).getResponse());
            return parser.getResult().get(0);

        }
    }

    public static String getLogin() throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(getDataFile()));
        while (true) {
            String s = scanner.nextLine();
            if (s.contains("login=")) return s.split("login=")[1];
        }
    }

    public static String getPass() throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(getDataFile()));
        while (true) {
            String s = scanner.nextLine();
            if (s.contains("pass=")) return s.split("pass=")[1];
        }
    }
}