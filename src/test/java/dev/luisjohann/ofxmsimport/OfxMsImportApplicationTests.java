package dev.luisjohann.ofxmsimport;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("dev")
class OfxMsImportApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(Boolean.TRUE);
	}

}
