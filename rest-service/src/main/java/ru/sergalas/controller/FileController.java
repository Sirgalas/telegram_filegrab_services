package ru.sergalas.controller;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sergalas.entity.AppDocument;
import ru.sergalas.entity.AppPhoto;
import ru.sergalas.entity.BinaryContent;
import ru.sergalas.service.interfaces.FileService;

import java.io.IOException;

@Log4j
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-doc")
    public void getDoc(@RequestParam("id") String id, HttpServletResponse response) {
        AppDocument doc = fileService.getDocument(id);
        if(doc == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ;
        }
        response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
        response.setHeader("Content-disposition","attachment; filename=" + doc.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);
        BinaryContent binaryContent = doc.getBinaryContent();
        try{
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        AppPhoto photo = fileService.getPhoto(id);
        if(photo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BinaryContent binaryContent = photo.getBinaryContent();
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition","attachment;");
        response.setStatus(HttpServletResponse.SC_OK);
        try{
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
