package com.dirsynch.directory_synchronizer_backend.controller;

import com.dirsynch.directory_synchronizer_backend.model.CFile;
import com.dirsynch.directory_synchronizer_backend.model.TgUser;
import com.dirsynch.directory_synchronizer_backend.repo.DataRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@AllArgsConstructor
@Slf4j
public class Controller {
    @Autowired
    private DataRepository repository;

    @RequestMapping(value = "/receive/{id}", method = RequestMethod.POST)
    public void get(HttpServletRequest request,
                    HttpServletResponse response,
                    @PathVariable("id") long id) throws IOException {
        byte[] byteArray = request.getInputStream().readAllBytes();
        log.info("Получено сообщение");
        CFile file = new TgUser();
        file.setID(id);
        file.setFile(byteArray);
        repository.saveFile(file);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public void info(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("INFO");
        response.setStatus(200);
        response.getWriter().write("SASHA :)");
        response.getWriter().flush();
    }
}
