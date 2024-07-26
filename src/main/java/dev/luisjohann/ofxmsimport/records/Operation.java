package dev.luisjohann.ofxmsimport.records;

import java.time.LocalDateTime;

/**
 * Operation
 */
public record Operation(String trnType, LocalDateTime dt, String value, String fiTid, String refNum, String memo) {
}