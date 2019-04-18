import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    Bot(boolean newUpdate){
        if(newUpdate) {
           sendUpdateMessage("Новые фишечки :)");
        }
    }


    public String getBotUsername() {
        return "githuby_bot_test";
    }



    public String getBotToken() {
        return BotConfig.TOKEN_TEST;
    }


    private void sendMsg(Message message, String text){
        SendMessage sendMsg = new SendMessage();
        sendMsg.enableMarkdown(true);
        sendMsg.setChatId(message.getChatId().toString());
        sendMsg.setText(text);
        try {
            setButton(sendMsg);
            execute(sendMsg);
        }catch (TelegramApiException e){
            System.out.println(e);
        }
    }


    private void setButton(SendMessage sendMessage){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(keyboardMarkup);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("podkovyrovda"));
        keyboardFirstRow.add(new KeyboardButton("pkyfen"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/start"));
        keyboardRows.add(keyboardFirstRow);
        keyboardRows.add(keyboardSecondRow);
        keyboardMarkup.setKeyboard(keyboardRows);
    }


    private void sendUpdateMessage(String text){
        ArrayList<String> userId = Data.ReadId("src/main/resources/allUsers.txt");
        for (String id : userId) {
            SendMessage update = new SendMessage();
            update.setText(text);
            update.setChatId(id);
            try {
                execute(update);
            } catch (TelegramApiException e) {
                System.out.println("пользователь " + id + " заблокировал бота");
            }
        }
    }


    public void onUpdateReceived(Update update) {
        GitModel model = new GitModel();
        Message message = update.getMessage();

        if(message != null && message.hasText()){
            switch (message.getText()){

                case "/help":
                    sendMsg(message, "Введите логин пользователя\nПример: pkyfen");
                    break;

                case "/start":
                    Data.InputInFile("src/main/resources/allUsers.txt", String.valueOf(message.getChatId()));
                    sendMsg(message, "Привет мир!\n" +
                            "Вы можете узнать последние действия пользователя в GetLastUpdates!" +
                            "\nПросто введите его логин" +
                            "\n version: 0.0.4");
                    break;

                case "супер":
                    sendMsg(message, "пупер!");
                    break;

                default:
                    try{
                        System.out.printf(message.getChat().getFirstName() + " " + message.getChat().getLastName()+ " => ");
                        sendMsg(message, GetLastUpdates.getUpdates(message.getText(), model));
                    } catch (IOException | ParseException e) {
                        sendMsg(message,"не смог найти такого пользователя");
                    }
            }
        }
    }

}
