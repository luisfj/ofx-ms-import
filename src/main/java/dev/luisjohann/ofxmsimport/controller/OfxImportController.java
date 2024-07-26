package dev.luisjohann.ofxmsimport.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webcohesion.ofx4j.io.OFXParseException;

import dev.luisjohann.ofxmsimport.clients.PermissionCheckerClient;
import dev.luisjohann.ofxmsimport.service.OfxImportFileService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OfxImportController {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(OfxImportController.class);

   final OfxImportFileService ofxImportFileService;
   final PermissionCheckerClient permissionCheckerClient;
   // final QueueSenderImportedFail queueImportFail;

   // @GetMapping("/file")
   // public Flux<Operation> importOfx() throws IOException, OFXParseException,
   // NotFoundException {
   // LOGGER.info("Import ofx");
   // // queueSender.send("Direct queue message");
   // // queueExchangeSender.convertAndSend(exchangeName, routingKeySse, "Exchange
   // // queue message PASS!!");

   // var message = new SseMessageDTO(1L, "Import Fail", "Evento disparado em
   // import file com FAIL no ms import",
   // LocalDateTime.now());

   // queueImportFail.send(message);
   // // var ofxResult = ofxReadService.readOfx();

   // // return Flux.fromIterable(ofxResult);
   // return Flux.empty();
   // }

   @PostMapping("/upload-ofx/{user_id}/{ue_id}")
   public Mono<ResponseEntity<String>> uploadOfxFile(@PathVariable("user_id") Long userId,
         @PathVariable("ue_id") Long ueId,
         @RequestParam("file") MultipartFile file) {
      try {
         var fileName = file.getOriginalFilename();

         LOGGER.info("Upload ofx file with name: '{}' for userId: {} and ueId: {}", fileName, userId, ueId);

         permissionCheckerClient.checkImportOfx(userId, ueId);

         var bts = file.getBytes();
         var fileSize = file.getSize();

         ofxImportFileService.addOfxFileToProccessQueue(bts, fileName, fileSize, userId, ueId);

         return Mono.just(ResponseEntity.ok().body("Arquivo incluído a fila de processamento"));

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (OFXParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (NotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return Mono.just(ResponseEntity.badRequest().body("Erro ao incluir arquivo a fila de processamento"));
      // "Arquivo carregado com sucesso! Estamos carregando o mesmo, assim que
      // processado você será notificado");
   }

   // public void store(MultipartFile file) {
   // try {
   // var rootLocation = Paths.get("upload-dir");
   // if (file.isEmpty()) {
   // throw new RuntimeException("Failed to store empty file.");
   // }
   // Path destinationFile = rootLocation.resolve(
   // Paths.get(file.getOriginalFilename()))
   // .normalize().toAbsolutePath();
   // if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
   // // This is a security check
   // throw new RuntimeException(
   // "Cannot store file outside current directory.");
   // }
   // try (InputStream inputStream = file.getInputStream()) {
   // Files.copy(inputStream, destinationFile,
   // StandardCopyOption.REPLACE_EXISTING);
   // }
   // } catch (IOException e) {
   // throw new RuntimeException("Failed to store file.", e);
   // }
   // }

}
