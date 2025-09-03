# ☕ 백엔드 개발 환경 설정 가이드

이 문서는 `studytrack-backend` 프로젝트의 개발 환경을 설정하는 전체 과정을 안내합니다.

---

### ✅ 사전 요구사항

- **Java 21 (JDK)**: [Eclipse Temurin 21 (OpenJDK)](https://adoptium.net/temurin/releases/?version=21) 또는 다른 OpenJDK 21 배포판을 설치합니다. 이 링크는 `design/spec.md`에 명시된 Java 21 버전에 해당합니다.
- **Gradle**: [Gradle 공식 홈페이지](https://gradle.org/install/)를 참고하여 설치합니다. (IDE에 내장된 Gradle을 사용해도 무방합니다.)
- **Docker & Docker Compose**: [Docker Desktop](https://www.docker.com/products/docker-desktop/)을 설치하여 데이터베이스 환경을 구성합니다.
- **IDE**: [IntelliJ IDEA](https://www.jetbrains.com/ko-kr/idea/) (Community 또는 Ultimate) 또는 VS Code with Java Extension Pack 사용을 권장합니다.

---


### 🚀 1단계: 프로젝트 클론 및 열기

1.  **Git을 사용하여 프로젝트를 클론합니다.**
    ```bash
    git clone [저장소_URL]
    ```

2.  **IDE에서 `studytrack-backend` 디렉토리를 프로젝트로 엽니다.**
    - IntelliJ의 경우, `build.gradle` 파일을 열면 자동으로 Gradle 프로젝트로 인식하고 필요한 의존성을 다운로드합니다.

---

### 🐘 2단계: 데이터베이스 설정 (PostgreSQL with Docker)

이 프로젝트는 Docker를 사용하여 PostgreSQL 데이터베이스를 실행합니다. 관련 설정은 `PostgreSQL-Docker.md` 문서에 더 상세히 기술되어 있습니다.

1.  **프로젝트 루트의 `docker-compose.yml` 파일을 확인합니다.** (없을 경우 `development/PostgreSQL-Docker.md` 참고하여 생성)

2.  **Docker 컨테이너를 백그라운드에서 실행합니다.**
    ```bash
    docker-compose up -d
    ```

3.  **데이터베이스가 정상적으로 실행 중인지 확인합니다.**
    ```bash
    docker ps
    ```
    *`studytrack-db`와 같은 이름의 컨테이너가 실행 중이어야 합니다.*

---

### ⚙️ 3단계: 애플리케이션 환경 설정

로컬 개발 환경에서는 `application-local.yml` 파일이 사용됩니다. 이 파일은 보통 Git에 포함되지 않으므로, 직접 생성해야 할 수 있습니다.

1.  **`src/main/resources` 디렉토리로 이동합니다.**

2.  **`application-local.yml` 파일을 생성하고 아래 내용을 작성합니다.**
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/studytrack_db # Docker에 설정된 DB명
        username: user # Docker에 설정된 사용자명
        password: password # Docker에 설정된 비밀번호
        driver-class-name: org.postgresql.Driver
      jpa:
        hibernate:
          ddl-auto: update # 개발 초기에는 update 또는 create 사용
        properties:
          hibernate:
            format_sql: true
            show_sql: true
      h2:
        console:
          enabled: true
          path: /h2-console

    # Swagger UI 활성화
    springdoc:
      swagger-ui:
        path: /swagger-ui.html
    ```

---

### ▶️ 4단계: 애플리케이션 실행 및 확인

1.  **Gradle을 사용하여 Spring Boot 애플리케이션을 실행합니다.**
    - IDE의 실행 버튼을 사용하거나, 터미널에서 아래 명령어를 입력합니다.
    ```bash
    ./gradlew bootRun
    ```

2.  **애플리케이션이 정상적으로 실행되었는지 확인합니다.**
    - **Swagger UI**: 브라우저에서 `http://localhost:8080/swagger-ui.html` 로 접속하여 API 문서를 확인합니다.
    - **H2 Console**: `ddl-auto: create` 사용 시, `http://localhost:8080/h2-console` 로 접속하여 인메모리 DB 상태를 확인할 수 있습니다. (JDBC URL: `jdbc:h2:mem:testdb`)

---

### ✅ 5단계: 테스트 실행

1.  **모든 테스트를 실행합니다.**
    ```bash
    ./gradlew test
    ```

2.  **테스트 커버리지 리포트를 생성하고 확인합니다.**
    ```bash
    # 테스트 실행 및 커버리지 리포트 생성
    ./gradlew jacocoTestReport

    # 리포트 확인 (브라우저에서 열기)
    # studytrack-backend/build/reports/jacoco/test/html/index.html
    ```
