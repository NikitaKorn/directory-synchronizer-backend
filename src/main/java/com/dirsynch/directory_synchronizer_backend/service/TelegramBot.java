package com.dirsynch.directory_synchronizer_backend.service;

import com.dirsynch.directory_synchronizer_backend.config.BotConfig;
import com.dirsynch.directory_synchronizer_backend.model.TgUser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    @Autowired
    private DataRepositoryService dataRepositoryService;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    startCommandHandler(update);
                    break;
                case "/load":
                    loadCommandHandler(chatId);
                    break;
            }
        }
    }

    public String processUser(Update update){
        Long userId = update.getMessage().getChatId();
        if(dataRepositoryService.contains(userId)){
            return "Пользователь с текущим ID уже зарегистрирован, ваш ID" + userId;
        } else {
            registerNewUser(update);
            return "Новый пользователь зарегистрирован, ваш ID " + userId;
        }
    }

    public void registerNewUser(Update update) {
        Long userId = update.getMessage().getChatId();
        String firstName = update.getMessage().getForwardSenderName();
        log.info("Register new user with name {}", firstName);
        TgUser newUser = new TgUser();
        newUser.setID(userId);
        newUser.setName(firstName);
        dataRepositoryService.saveFile(newUser);
        log.info("New user with name {} was registered", firstName);
    }

    public void startCommandHandler(Update update) throws TelegramApiException {
        String text = processUser(update);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText(text);

        execute(message);
    }

    public void loadCommandHandler(long chatId) throws TelegramApiException, IOException {
        String path = String.format("src/main/resources/files/%s.zip", chatId);

        byte[] byteArray = dataRepositoryService.loadFile(chatId).getFile();

        File f = new File(path);
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(byteArray);
        fos.flush();
        fos.close();

        InputFile file = new InputFile();
        file.setMedia(new File(path));

        SendDocument document = new SendDocument();
        document.setChatId(String.valueOf(chatId));
        document.setDocument(file);

        execute(document);
    }
}
