import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {
    public static void InputInFile(String fileName, String id) {
        ArrayList<String> usersId = ReadId(fileName);
        if (!usersId.contains(id)) {
            try {
                FileWriter writer = new FileWriter(fileName, true);
                writer.append(id).append("\n");
                writer.flush();
                System.out.println("Новый пользователь " + id);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
