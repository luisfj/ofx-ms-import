package dev.luisjohann.ofxmsimport.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.webcohesion.ofx4j.io.OFXParseException;

import dev.luisjohann.ofxmsimport.queue.QueueSenderImportedFail;
import dev.luisjohann.ofxmsimport.queue.QueueSenderImportedSuccess;
import dev.luisjohann.ofxmsimport.queue.QueueSenderSse;
import dev.luisjohann.ofxmsimport.records.Operation;
import dev.luisjohann.ofxmsimport.records.SseImportedMessageDTO;
import dev.luisjohann.ofxmsimport.records.SseMessageDTO;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfxImportFileService {
   private static final Logger LOGGER = LoggerFactory.getLogger(OfxImportFileService.class);

   final OfxReadService readService;
   final QueueSenderImportedFail queueImportFail;
   final QueueSenderImportedSuccess queueImportSuccess;
   final QueueSenderSse queueSse;

   @Async
   public CompletableFuture<List<Operation>> addOfxFileToProccessQueue(byte[] fileByteArray, String fileName,
         Long fileSize, Long userId, Long ueId)
         throws InterruptedException, IOException, OFXParseException, NotFoundException {
      LOGGER.info("Import File: '{}' with size '{}' for user: '{}' and ue: '{}' ", fileName,
            FileUtils.byteCountToDisplaySize(fileSize), userId, ueId);

      return importOfxFuture(fileByteArray)
            .whenCompleteAsync((operations, error) -> {
               if (Objects.nonNull(error)) {
                  LOGGER.error(error.getMessage(), error);
                  var message = new SseMessageDTO(userId, "Import Fail", error.getMessage(), LocalDateTime.now(), null);
                  queueImportFail.send(message);
               } else {
                  // ObjectMapper mapper = new ObjectMapper()
                  // .findAndRegisterModules()
                  // .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                  // String json = "";
                  // try {
                  // json = mapper.writeValueAsString(operations);
                  // } catch (JsonProcessingException e) {
                  // // TODO Auto-generated catch block
                  // e.printStackTrace();
                  // }

                  var message = new SseMessageDTO(userId, "Import Success", "File is proccessed successfully",
                        LocalDateTime.now(), null);

                  var importedMessage = new SseImportedMessageDTO(userId, userId, ueId, fileName,
                        LocalDateTime.now(), operations);

                  queueImportSuccess.send(importedMessage);
                  queueSse.send(message);
               }
            });
   }

   @Async
   private CompletableFuture<List<Operation>> importOfxFuture(byte[] fileByteArray)
         throws InterruptedException, IOException, OFXParseException, NotFoundException {

      // Artificial delay of 1s for demonstration purposes
      Thread.sleep(1000L);
      var operations = readService.readOfx(fileByteArray);
      Thread.sleep(RandomGenerator.getDefault().nextLong(1000, 10000));
      return CompletableFuture.completedFuture(operations);
   }

}
