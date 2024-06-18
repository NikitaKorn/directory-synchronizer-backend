package com.dirsynch.directory_synchronizer_backend.model;

import lombok.Data;

@Data
public abstract class CFile {
    private long ID;
    private byte[] file;
}
