import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    public static void InputInFile(String fileName, String id){
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(id + "\n");
            writer.flush();
            System.out.println("Новый пользователь " + id);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> ReadId(String fileName){
        ArrayList<String> usersId = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(fileName));
            while (scan.hasNext()){
                usersId.add(scan.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return usersId;
    }

}
