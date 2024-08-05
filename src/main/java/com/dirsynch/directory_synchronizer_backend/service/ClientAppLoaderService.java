package com.dirsynch.directory_synchronizer_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ClientAppLoaderService {
    @Value("${service.files.repository-path}")
    private String repositoryPath;
    private final String fileName = "Client-portable-1.0.0.rar";

    public File loadFile()  {
        return new File(repositoryPath+fileName);
    }
}
