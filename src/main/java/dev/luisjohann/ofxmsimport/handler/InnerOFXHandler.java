package dev.luisjohann.ofxmsimport.handler;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.webcohesion.ofx4j.io.OFXHandler;
import com.webcohesion.ofx4j.io.OFXSyntaxException;

import dev.luisjohann.ofxmsimport.enums.AgregateType;
import dev.luisjohann.ofxmsimport.records.Operation;

public class InnerOFXHandler implements OFXHandler {

   final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
   final List<Operation> operations = new ArrayList<>();
   String trnType, value, fiTid, refNum, memo;
   LocalDateTime dt;
   boolean reading;

   public List<Operation> getOperations() {
      return operations;
   }

   @Override
   public void onHeader(String name, String value) throws OFXSyntaxException {
      // System.out.println(String.format("onHead %s: %s", name, value));
   }

   @Override
   public void onElement(String name, String value) throws OFXSyntaxException {
      // System.out.println(String.format("onElement %s: %s", name, value));
      try {
         if (reading)
            processElement(name, value);
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void startAggregate(String aggregateName) throws OFXSyntaxException {
      if (!aggregateName.equals(AgregateType.STMTTRN.name()))
         return;

      reading = true;
      trnType = null;
      dt = null;
      value = null;
      fiTid = null;
      refNum = null;
      memo = null;

   }

   @Override
   public void endAggregate(String aggregateName) throws OFXSyntaxException {
      reading = false;
      if (aggregateName.equals(AgregateType.STMTTRN.name()))
         operations.add(new Operation(trnType, dt, value, fiTid, refNum, memo));
   }

   private void processElement(String elementName, String elementValue) throws ParseException {
      switch (elementName) {
         case "TRNTYPE" -> trnType = elementValue;
         case "DTPOSTED" -> dt = converterLocalDateTime(elementValue);
         case "TRNAMT" -> value = elementValue;
         case "FITID" -> fiTid = elementValue;
         case "REFNUM" -> refNum = elementValue;
         case "MEMO" -> memo = elementValue;
      }
   }

   private LocalDateTime converterLocalDateTime(String strDate) {
      return LocalDateTime.parse(strDate.replaceAll("\\[.*\\]", ""), formatter);
   }
}