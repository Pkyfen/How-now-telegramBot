import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
            telegramBotsApi.registerBot(new Bot());
        }catch (TelegramApiException e){
            System.out.println(e);
        }
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMsg = new SendMessage();
        sendMsg.enableMarkdown(true);
        sendMsg.setChatId(message.getChatId().toString());
//        sendMsg.setReplyToMessageId(message.getMessageId());
        sendMsg.setText(text);
        try {
            setButton(sendMsg);
            execute(sendMsg);
        }catch (TelegramApiException e){
            System.out.println(e);
        }
    }

    public void setButton(SendMessage sendMessage){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(keyboardMarkup);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("podkovyrovda"));
        keyboardFirstRow.add(new KeyboardButton("pkyfen"));

        keyboardRows.add(keyboardFirstRow);
        keyboardMarkup.setKeyboard(keyboardRows);
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
                    sendMsg(message, "Привет мир!\n" +
                            "Вы можете узнать последние действия пользователя в GitHub!" +
                            "\nПросто введите его логин" +
                            "\n version: 0.0.3");
                    break;
                case "супер":
                    sendMsg(message, "пупер!");
                 default:
                     try{
                         sendMsg(message, GitHub.getUpdates(message.getText(), model));
                     } catch (IOException | ParseException e) {
                         sendMsg(message,"не смог найти такого пользователя");
                     }
            }
        }
    }

    public String getBotUsername() {
        return "githuby_bot";
    }

    public String getBotToken() {
        return "710491839:AAEHZvDYMGxc_EZG3Uy1WeFKOYu3fEg7xuc";
    }
}
