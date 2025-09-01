package com.studytrack.studytrackbackend;

import com.studytrack.studytrackbackend.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "jwt.secret=testSecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong",
    "web.cors.allowed-origins=http://localhost:3000",
    "web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS"
})
class StudytrackBackendApplicationTests {

    @Test
    void contextLoads() {
        // 단순 컨텍스트 로딩 테스트
    }
}
