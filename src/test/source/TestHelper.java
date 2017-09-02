package test.source;

import engine.connector.HttpConnection;
import engine.connector.HttpsConnection;
import engine.connector.Method;
import engine.parser.JsonParser;
import engine.parser.XmlParser;
import source.boor.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;


public class TestHelper {
//TODO: do something with uploading tests

    private static File getDataFile(){
        return new File(new File("").getAbsolutePath() + "\\src\\Data");
    }

    public static HashMap<String, String> getDataFromBoorAdvanced(AbstractBoorAdvanced boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);

        HttpsConnection connection = new HttpsConnection()
                .setRequestMethod(Method.GET)
                .setUserAgent(HttpsConnection.getDefaultUserAgent())
                .openConnection(request1);


        JsonParser parser = new JsonParser();

        parser.startParse(connection.getResponse());

        return parser.getResult().get(0);
    }

    public static HashMap<String, String> getDataFromBoorBasic(AbstractBoorBasic boor, int id) throws Exception {
        String request1 = boor.getPostByIdRequest(id);
        //System.out.println(request1);

        XmlParser parser = new XmlParser();

        parser.startParse(request1);
        return parser.getResult().get(0);
    }

    public static String getLogin() throws Exception{
        Scanner scanner = new Scanner(new FileInputStream(getDataFile()));
        while(true){
            String s = scanner.nextLine();
            if (s.contains("login=")) return s.split("login=")[1];
        }
    }

    public static String getPass() throws Exception{
        Scanner scanner = new Scanner(new FileInputStream(getDataFile()));
        while(true){
            String s = scanner.nextLine();
            if (s.contains("pass=")) return s.split("pass=")[1];
        }
    }
}