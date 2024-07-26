package dev.luisjohann.ofxmsimport.records;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record SseImportedMessageDTO(Long userId, Long userImportedId, Long ueId, String fileName,
            LocalDateTime dateTime,
            List<Operation> operations) implements Serializable {

}