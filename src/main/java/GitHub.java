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
//        URL url = new URL("https://api.github.com/repos/Pkyfen/MyTrackyJava");
        Scanner in = new Scanner((InputStream) url.getContent());
        while(in.hasNext()){
            result  += in.nextLine();
        }
//        System.out.println("Получил "+ result+"\n\n\n");

        JSONArray array = new JSONArray(result);
//        System.out.println("Преобразовал "+array);
        for(int i = 0 ; i < 5; i++){
            JSONObject object = array.getJSONObject(i);
            model.setType(object.getString("type"));
            JSONObject repo = object.getJSONObject("repo");
            model.setUrl(getHttpUrl(repo.getString("url")));
            model.setRepo(repo.getString("name"));
            JSONObject actor = object.getJSONObject("actor");
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
           answer+=(
                   "__"+(i+1)+") "+
                    "Действие " + model.getType()+ "__\n    "+
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
