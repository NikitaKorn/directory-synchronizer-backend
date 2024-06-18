package com.dirsynch.directory_synchronizer_backend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class File {
    private Long id;
    private byte[] file;
}
