package com.dirsynch.directory_synchronizer_backend.repo;

import com.dirsynch.directory_synchronizer_backend.model.CFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository
public class FilesystemDataRepository implements DataRepository {
    @Value("${service.files.repository-path}")
    private String repositoryPath;

    @Override
    public void saveFile(CFile file) throws IOException {
        String fn = repositoryPath + file.getID() + ".zip";
        FileUtils.writeByteArrayToFile(new File(fn), file.getFile());
    }

    @Override
    public CFile loadFile(Long id) {
        return null;
    }

    @Override
    public boolean contains(Long id) {
        return false;
    }
}
