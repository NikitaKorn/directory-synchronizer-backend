package com.dirsynch.directory_synchronizer_backend.service;


import com.dirsynch.directory_synchronizer_backend.model.CFile;
import com.dirsynch.directory_synchronizer_backend.repo.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataRepositoryService {
    @Autowired
    private DataRepository repository;

    public void saveFile(CFile file){
        try {
            repository.saveFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CFile loadFile(Long id){
        return repository.loadFile(id);
    }

    public boolean contains(Long id){
        return repository.contains(id);
    }
}
