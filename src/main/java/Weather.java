import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //c7f08d0feb3b038dd430872927158fc1
    public static String getWeather(String city, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=c7f08d0feb3b038dd430872927158fc1");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while(in.hasNext()){
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));
        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumiditly(main.getDouble("humidity"));
        JSONArray array = object.getJSONArray("weather");
        for(int i=0; i<array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            model.setMain(obj.getString("main"));
        }

        System.out.println(model.getName());
        return "City: " +model.getName() + "\ntemperature: "
                + model.getTemp() + "C\nhumidity: "
                + model.getHumiditly() +"%\nmain: "
                + model.getMain();
    }
}
