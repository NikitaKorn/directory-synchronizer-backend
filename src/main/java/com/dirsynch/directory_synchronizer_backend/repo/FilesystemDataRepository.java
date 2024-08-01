package com.dirsynch.directory_synchronizer_backend.repo;

import com.dirsynch.directory_synchronizer_backend.model.CFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

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
    public File loadFile(Long id)  {
        File f = new File(repositoryPath);
        File[] matchingFiles = f.listFiles((dir, name) -> name.startsWith(String.valueOf(id)));
        Optional<File> first = Arrays.stream(matchingFiles).findFirst();
        return first.orElse(null);
    }
}
