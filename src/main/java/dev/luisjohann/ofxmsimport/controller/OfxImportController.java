package dev.luisjohann.ofxmsimport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.ofxmsimport.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsimport.service.OfxImportFileService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OfxImportController {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(OfxImportController.class);

   final OfxImportFileService ofxImportFileService;
   final PermissionCheckerClient permissionCheckerClient;

   @PostMapping("/upload-ofx/{user_id}/{ue_id}")
   public Mono<ResponseEntity<String>> uploadOfxFile(@PathVariable("user_id") Long userId,
         @PathVariable("ue_id") Long ueId,
         @RequestPart("file") Flux<FilePart> filePartFlux) {

      return filePartFlux.flatMap(filePart -> {
         return DataBufferUtils.join(filePart.content())
               .map(dataBuffer -> {
                  var fileSize = dataBuffer.readableByteCount();
                  byte[] bts = new byte[fileSize];
                  dataBuffer.read(bts);

                  try {
                     ofxImportFileService.addOfxFileToProccessQueue(bts, filePart.filename(), Long.valueOf(fileSize),
                           userId, ueId);
                     return ResponseEntity.ok().body("Arquivo incluído a fila de processamento");
                  } catch (Exception e) {
                     LOGGER.error(e.getMessage(), e);
                  }
                  return ResponseEntity.badRequest().body("Erro ao incluir arquivo a fila de processamento");
               });
      }).next();
   }

}
