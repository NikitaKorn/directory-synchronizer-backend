package com.dirsynch.directory_synchronizer_backend.service;


import com.dirsynch.directory_synchronizer_backend.model.CFile;
import com.dirsynch.directory_synchronizer_backend.repo.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class DataRepositoryService {
    @Autowired
    private DataRepository repository;

    public void saveFile(CFile file){
        try {
            log.info("Пользователь сохранил файл!");
            repository.saveFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File loadFile(Long id) {
        log.info("Загрузка файла с id {}", id);
        return repository.loadFile(id);
    }
}
