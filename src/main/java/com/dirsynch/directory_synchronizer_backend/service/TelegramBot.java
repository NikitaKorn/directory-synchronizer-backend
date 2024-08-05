package com.dirsynch.directory_synchronizer_backend.service;

import com.dirsynch.directory_synchronizer_backend.config.BotConfig;
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
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    private final String linkOnFile = "https://disk.yandex.ru/d/r4URL8aI_vgrxg";
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

    public void startCommandHandler(Update update) throws TelegramApiException, IOException {
        SendMessage message = new SendMessage();
        String chatId = String.valueOf(update.getMessage().getChatId());
        message.setChatId(chatId);
        message.setText(String.format("Your chat id is %s!\nUse this in your client application.\nYou can load it from here %s", chatId, linkOnFile));
        execute(message);
    }

    public void loadCommandHandler(long chatId) throws TelegramApiException, IOException {
        File file = dataRepositoryService.loadFile(chatId);
        if(file == null){
            sendMessageToUser(chatId);
        } else {
            sendDocumentToUser(file, chatId);
        }
    }

    private void sendMessageToUser(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("User wasn't found!");
        execute(message);
    }

    private void sendDocumentToUser(File file, Long chatId) throws IOException, TelegramApiException {
        byte[] byteArray = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(byteArray);
        fos.flush();
        fos.close();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);

        SendDocument document = new SendDocument();
        document.setChatId(String.valueOf(chatId));
        document.setDocument(inputFile);

        execute(document);
    }
}
