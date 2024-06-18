package com.dirsynch.directory_synchronizer_backend.repo;

import com.dirsynch.directory_synchronizer_backend.model.CFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InMemoryDataRepository implements DataRepository {
    private Map<Long, CFile> files;

    public InMemoryDataRepository() {
        this.files = new HashMap<>();
    }

    @Override
    public void saveFile(CFile file) throws IOException {
        files.put(file.getID(), file);
    }

    @Override
    public CFile loadFile(Long id) {
        return files.get(id);
    }

    public boolean contains(Long id){
        return files.containsKey(id);
    }
}
