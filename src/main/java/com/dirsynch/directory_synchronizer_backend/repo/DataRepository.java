package com.dirsynch.directory_synchronizer_backend.repo;

import com.dirsynch.directory_synchronizer_backend.model.CFile;

import java.io.File;
import java.io.IOException;

public interface DataRepository {

    void saveFile(CFile file) throws IOException;

    File loadFile(Long id);

    boolean contains(Long id);
}
