import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

public class TestGetLustUpdates {
    private static String lastUpdates;

    private static String type;
    private static String url;
    private static String login;
    private static String date;
    private static String repository;
    private static String commit;

    public static void main(String[] args) throws IOException, ParseException {
        getLastUpdates("pkyfen", new GitModel());
    }
    public static void getLastUpdates(String acc, GitModel model) throws IOException, ParseException {
        lastUpdates = "*Пользователь"+acc+"*\n\n";
        String informationFromUrl = getInformationFromUrl(
                new URL("https://api.github.com/users/"
                        + acc + "/events/public"));
        JSONArray information = new JSONArray(informationFromUrl);
        for(int numberOfUpdate = 0; numberOfUpdate<5 ; numberOfUpdate++){
            getUpdates(information, numberOfUpdate);
            setUpdates(model);
            createMessage(model);
        }
        System.out.println(lastUpdates);
    }

    private static void createMessage(GitModel model) {
        lastUpdates+=model.getLogin() +" " + model.getDate()+ " "+ model.getType()+"\n\n";
    }

    private static void setUpdates(GitModel model) throws ParseException {
        model.setLogin(login);
        model.setType(type);
        model.setDate(date);
        model.setUrl(url);
        model.setRepo(repository);
    }


    private static void getUpdates(JSONArray array, int numberOfUpdates){
        JSONObject object = array.getJSONObject(numberOfUpdates);
        JSONObject repo = object.getJSONObject("repo");
        JSONObject actor = object.getJSONObject("actor");

        type = object.getString("type");
        url = repo.getString("url");
        repository = repo.getString("name");
        login = actor.getString("login");
        date = object.getString("created_at");
    }
    private static String getInformationFromUrl(URL url) throws IOException {
        String info = "";
        Scanner scan = new Scanner((InputStream) url.getContent());
        while(scan.hasNext()){
            info  += scan.nextLine();
        }
        return info;
    }
}
