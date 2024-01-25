package ru.sergalas.controller;


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

@Log4j
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        AppDocument doc = fileService.getDocument(id);
        if(doc == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = doc.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if(fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + doc.getDocName())
                .body(fileSystemResource);
    }

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        AppPhoto photo = fileService.getPhoto(id);
        if(photo == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = photo.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if(fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().
                contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
