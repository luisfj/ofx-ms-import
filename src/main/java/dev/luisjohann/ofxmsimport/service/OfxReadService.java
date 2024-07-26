package dev.luisjohann.ofxmsimport.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.webcohesion.ofx4j.io.OFXParseException;
import com.webcohesion.ofx4j.io.nanoxml.NanoXMLOFXReader;

import dev.luisjohann.ofxmsimport.handler.InnerOFXHandler;
import dev.luisjohann.ofxmsimport.records.Operation;
import javassist.NotFoundException;

@Service
public class OfxReadService {

   public List<Operation> readOfx(byte[] fileByteArray)
         throws IOException, OFXParseException, NotFoundException {
      var handler = new InnerOFXHandler();

      try (var fIn = removeBadCharacters(fileByteArray)) {
         var reader = new NanoXMLOFXReader();
         reader.setContentHandler(handler);
         reader.parse(fIn);
      }
      // handler.getOperations().forEach(System.out::println);
      return handler.getOperations();
   }

   // public List<Operation> readOfx() throws IOException, OFXParseException,
   // NotFoundException {
   // var file = ResourceUtils.getFile("classpath:sicredi.ofx");
   // if (!file.exists())
   // throw new NotFoundException("Arquivo OFX não encontrado!");

   // var handler = new InnerOFXHandler();

   // try (var fIn = new FileInputStream(file)) {
   // var reader = new NanoXMLOFXReader();
   // reader.setContentHandler(handler);
   // reader.parse(fIn);
   // }

   // handler.getOperations().forEach(System.out::println);
   // return handler.getOperations();
   // }

   private InputStream removeBadCharacters(byte[] fileByteArray) throws IOException {

      final Map<String, String> charChanges = Map.of("&", "e");

      Reader fr = new InputStreamReader(new ByteArrayInputStream(fileByteArray),
            StandardCharsets.US_ASCII);
      String s;
      String totalStr = "";
      try (BufferedReader br = new BufferedReader(fr)) {

         while ((s = br.readLine()) != null) {
            totalStr += s;
         }

         for (var search : charChanges.keySet()) {
            totalStr = totalStr.replaceAll(search, charChanges.get(search));
         }
      }
      return new ByteArrayInputStream(totalStr.getBytes());

   }
}
