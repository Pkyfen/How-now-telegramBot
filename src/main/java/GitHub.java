import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;

public class GitHub {
    public static String getUpdates(String acc, GitModel model) throws IOException, ParseException {
        String answer="Пользователь "+acc+"\n\n";
        String result="";
        try{
        URL url =
                new URL("https://api.github.com/users/"+acc+"/events/public");
        Scanner in = new Scanner((InputStream) url.getContent());
        while(in.hasNext()){
            result  += in.nextLine();
        }

        JSONArray array = new JSONArray(result);
        for(int i = 0 ; i < 5; i++){
            JSONObject object = array.getJSONObject(i);
            JSONObject repo = object.getJSONObject("repo");
            JSONObject actor = object.getJSONObject("actor");

            model.setType(object.getString("type"));
            model.setUrl(getHttpUrl(repo.getString("url")));
            model.setRepo(repo.getString("name"));
            model.setLogin(actor.getString("login"));
            try{
                JSONArray commits = object.getJSONObject("payload").getJSONArray("commits");
                JSONObject message = commits.getJSONObject(0);
                model.setCommit(message.getString("message"));
            }
            catch (Exception e){
                model.setCommit("null");
            }
            model.setDate(object.getString("created_at"));
//
           answer+=("_"+(i+1)+") "+
                    "Действие " + model.getType()+ "_\n    "+
                           model.getDate()+"\n    "+
                     "Комментарий " + model.getCommit()+"\n    "+
                    "В репозитории ["+ model.getRepo()+ "]("+ model.getUrl()+")\n\n");
        }
        System.out.println(model.getLogin());}
        catch (Exception e){
            return "пользователь не найден";
        }
        return answer;

    }

    private static String getHttpUrl(String url) throws IOException {
        String result = "";
        URL apiUrl = new URL(url);
        Scanner in = new Scanner((InputStream) apiUrl.getContent());
        while(in.hasNext()){
            result  += in.nextLine();
        }
        JSONObject object = new JSONObject(result);

        return object.getString("html_url");


    }
}
