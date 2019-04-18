import org.glassfish.grizzly.streams.Input;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;

public class GetLastUpdates {
    private String answer = "*Пользователь \"+acc+\"*\\n\\n";
    private String result = "";
    public static String getUpdates(String acc, GitModel model) throws IOException, ParseException {
        String answer="*Пользователь "+acc+"*\n\n";
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
                String commit = "";
                for(int k = 0; k < commits.length(); k++) {
                    JSONObject message = commits.getJSONObject(k);
                    if(message.getBoolean("distinct")) {
                        if(commits.length()>1 && k+1!=commits.length()) {
                            commit +=  message.getString("message")+",\n";
                        }else{
                            commit += message.getString("message");
                        }
                    }
                }
                model.setCommit(commit);
            }
            catch (Exception e){
                model.setCommit("null");
            }

            model.setDate(object.getString("created_at"));

            switch (object.getString("type")){
                case "PushEvent":
                    answer+=("_"+(i+1)+") "+
                            rusType(model.getType())+ "_\n    "+
                            "Изменения: " + model.getCommit()+"\n    " +
                            "В репозитории ["+ model.getRepo()+ "]("+ model.getUrl()+")\n    "+
                            model.getDate() + "\n\n");
                            break;

                case"CreateEvent":
                    answer+=("_"+(i+1)+") "+
                            rusType(model.getType())+ "_\n    "+
                            "["+ model.getRepo()+ "]("+ model.getUrl()+")\n    "+
                            model.getDate()+"\n\n");
                    break;

                case "WatchEvent":
                    answer+=("_"+(i+1)+") "+
                            rusType(model.getType())+ "_\n    "+
                            "Репозиторий "+"["+ model.getRepo()+ "]("+ model.getUrl()+")\n    "+
                            model.getDate()+"\n\n");
                    break;

                case"IssueCommentEvent":
                    answer+=("_"+(i+1)+") "+
                            rusType(model.getType())+ "_\n    "+
                            "В репозиторий "+"["+ model.getRepo()+ "]("+ model.getUrl()+")\n    "+
                            model.getDate()+"\n\n");
                    break;

                    default:answer+=("_"+(i+1)+") "+
                            "Действие " + model.getType()+ "_\n    "+
                            "В репозитории ["+ model.getRepo()+ "]("+ model.getUrl()+")" +"\n"+
                            model.getDate()+"\n\n");
                    break;
            }
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

    private static String rusType(String engType){
        Map<String, String> type =
                Map.of("PushEvent","Отправил коммит",
                        "CreateEvent","Создал репозиторий",
                        "WatchEvent","Начал отслеживать",
                        "IssueCommentEvent","Отправил ишью");
        return type.get(engType);
    }

    private static String getInformationFromUrl(URL url) throws IOException {
        String info = "";
        Scanner scan = new Scanner((InputStream) url.getContent());
        while(scan.hasNext()){
            info  += scan.nextLine();
        }
        return info;
    }

//    private void setUpdates(JSONArray array, GitModel gitModel, int numberOfUpdates){
//        JSONObject object = array.getJSONObject(numberOfUpdates);
//        JSONObject repo = object.getJSONObject("repo");
//        JSONObject actor = object.getJSONObject("actor");
//
//        model.setType(object.getString("type"));
//        model.setUrl(getHttpUrl(repo.getString("url")));
//        model.setRepo(repo.getString("name"));
//        model.setLogin(actor.getString("login"));
//    }
}
