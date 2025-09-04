# 🔧 정적 분석 도구 및 CI/CD 설정 완전 가이드

이 문서는 StudyTrack 프로젝트의 모든 정적 분석 도구와 CI/CD 설정을 상세히 설명합니다.

---

## 📋 목차

1. [백엔드 설정](#-백엔드-설정)
2. [프론트엔드 설정](#-프론트엔드-설정)
3. [GitHub Actions CI/CD](#-github-actions-cicd)
4. [설정 파일별 상세 분석](#-설정-파일별-상세-분석)

---

## 🚀 백엔드 설정

### 사용 중인 정적 분석 도구

| 도구 | 목적 | 설정 파일 | 실행 명령어 |
|------|------|-----------|-------------|
| **Checkstyle** | 코드 스타일 검사 | `config/checkstyle/checkstyle.xml` | `./gradlew checkstyleMain` |
| **SpotBugs** | 잠재적 버그 탐지 | `build.gradle` | `./gradlew spotbugsMain` |
| **PMD** | 코드 품질 분석 | `config/pmd/pmd-rules.xml` | `./gradlew pmdMain` |
| **JaCoCo** | 테스트 커버리지 | `build.gradle` | `./gradlew jacocoTestReport` |

### build.gradle 주요 설정 분석

```gradle
plugins {
    id 'java'                                          // Java 언어 지원
    id 'org.springframework.boot' version '3.5.4'     // Spring Boot 프레임워크
    id 'io.spring.dependency-management' version '1.1.7'  // 의존성 관리
    id 'checkstyle'                                    // 코드 스타일 검사 도구
    id 'com.github.spotbugs' version '6.0.26'         // 정적 분석 도구 (버그 탐지)
    id 'jacoco'                                        // 테스트 커버리지 측정
    id 'pmd'                                           // 코드 품질 분석 도구
}
```

#### Java 버전 설정
```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)   // Java 21 사용 강제
    }
}
```
- **목적**: 프로젝트 전체에서 Java 21을 사용하도록 보장
- **효과**: 개발자 환경이 다르더라도 동일한 Java 버전 사용

#### Checkstyle 설정
```gradle
checkstyle {
    toolVersion = '10.12.4'                           // Checkstyle 버전
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")  // 규칙 파일 위치
}

checkstyleMain {
    source = 'src/main/java'                          // 메인 소스 대상
}

checkstyleTest {
    source = 'src/test/java'                          // 테스트 소스 대상
}
```
- **목적**: Google Java Style Guide 기반 코드 스타일 강제
- **검사 대상**: 들여쓰기, 네이밍, 라인 길이, 공백 등

#### SpotBugs 설정
```gradle
spotbugs {
    toolVersion = '4.8.2'                            // SpotBugs 버전
    effort = 'max'                                    // 최대 분석 강도
    reportLevel = 'medium'                            // 중간 수준 이상 문제만 보고
}

spotbugsMain {
    reports {
        xml.required = false                          // XML 리포트 비활성화
        html.required = true                          // HTML 리포트 활성화
    }
}
```
- **목적**: 잠재적 버그, 성능 문제, 보안 취약점 탐지
- **분석 수준**: 최대 강도로 분석하되 중간 수준 이상만 보고

#### PMD 설정
```gradle
pmd {
    toolVersion = '6.55.0'                           // PMD 버전
    consoleOutput = true                              // 콘솔에 결과 출력
    ruleSetFiles = files("config/pmd/pmd-rules.xml") // 커스텀 규칙 파일
    ruleSets = []                                     // 기본 규칙셋 비활성화
}
```
- **목적**: 코드 품질, 복잡도, 네이밍 규칙 등 종합 분석
- **규칙**: Spring Boot에 최적화된 커스텀 규칙 적용

#### JaCoCo 설정
```gradle
jacoco {
    toolVersion = '0.8.11'                           // JaCoCo 버전
}

jacocoTestReport {
    reports {
        xml.required = true                           // CI용 XML 리포트
        html.required = true                          // 개발자용 HTML 리포트
        csv.required = false                          // CSV 리포트 비활성화
    }
    finalizedBy jacocoTestCoverageVerification       // 리포트 후 검증 실행
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.70                        // 전체 70% 최소 커버리지
            }
        }
        rule {
            enabled = true
            element = 'CLASS'                         // 클래스 단위 검사
            excludes = [
                '*.dto.*',                            // DTO 클래스 제외
                '*.config.*',                         // 설정 클래스 제외
                '*.StudytrackBackendApplication*'     // 메인 클래스 제외
            ]
            limit {
                counter = 'LINE'                      // 라인 커버리지 기준
                value = 'COVEREDRATIO'
                minimum = 0.60                        // 클래스별 60% 최소 커버리지
            }
        }
    }
}
```
- **목적**: 테스트 커버리지 측정 및 최소 기준 강제
- **기준**: 전체 70%, 개별 클래스 60% 최소 커버리지

### .editorconfig 설정 분석

```ini
# StudyTrack Backend - Java 코딩 스타일 설정
# Google Java Style Guide 기반
root = true                                           # 이 파일이 최상위 설정

# 모든 파일 공통 설정
[*]
charset = utf-8                                       # UTF-8 인코딩 사용
end_of_line = lf                                      # Unix 스타일 줄바꿈 (\n)
insert_final_newline = true                           # 파일 끝에 빈 줄 추가
trim_trailing_whitespace = true                       # 줄 끝 공백 제거

# Java 소스 파일
[*.java]
indent_style = space                                  # 공백으로 들여쓰기
indent_size = 4                                       # 4칸 들여쓰기
max_line_length = 120                                 # 최대 120자 라인 길이
ij_java_use_single_class_imports = true               # 개별 클래스 import 사용
ij_java_class_count_to_use_import_on_demand = 999    # * import 사용 임계값 (사실상 사용 안함)
ij_java_import_layout = *,|,javax.**,java.**,|,$*    # import 순서 규칙
```
- **목적**: IDE 간 일관된 코딩 스타일 보장
- **기준**: Google Java Style Guide 준수

---

## ⚛️ 프론트엔드 설정

### 사용 중인 정적 분석 도구

| 도구 | 목적 | 설정 파일 | 실행 명령어 |
|------|------|-----------|-------------|
| **ESLint** | 코드 품질 및 스타일 | `eslint.config.js` | `npm run lint` |
| **Prettier** | 코드 포매팅 | `.prettierrc.json` | `npx prettier --check .` |
| **TypeScript** | 타입 체크 | `tsconfig.json` | `npx tsc --noEmit` |
| **Vitest** | 테스트 및 커버리지 | `vite.config.ts` | `npm run test:coverage` |

### eslint.config.js 주요 설정 분석

#### 플러그인 import
```javascript
import js from '@eslint/js'                          // JavaScript 기본 규칙
import globals from 'globals'                        // 전역 변수 정의
import reactHooks from 'eslint-plugin-react-hooks'   // React Hooks 규칙
import reactRefresh from 'eslint-plugin-react-refresh' // Vite HMR 규칙
import react from 'eslint-plugin-react'              // React 규칙
import jsxA11y from 'eslint-plugin-jsx-a11y'        // 접근성 규칙
import tseslint from 'typescript-eslint'             // TypeScript 규칙
```
- **이유**: React + TypeScript 프로젝트에 필요한 모든 규칙 플러그인

#### 무시 패턴 설정
```javascript
{
  ignores: [
    'dist/**',                                        // 빌드 결과물
    'build/**',                                       // 빌드 디렉토리
    'node_modules/**',                                // 의존성 패키지
    'coverage/**',                                    // 커버리지 리포트
    '.storybook/**',                                  // Storybook 설정
    'storybook-static/**',                            // Storybook 빌드
    '*.config.js',                                    // 설정 파일들
    '*.config.ts',
    'vite.config.ts'
  ]
}
```
- **목적**: 검사 불필요한 파일들 제외로 성능 향상

#### TypeScript 파일 규칙
```javascript
{
  files: ['**/*.{js,mjs,cjs,ts,tsx}'],               // 대상 파일 패턴
  languageOptions: {
    ecmaVersion: 2022,                                // ES2022 문법 지원
    globals: {
      ...globals.browser,                             // 브라우저 전역 변수
      ...globals.es2022,                              // ES2022 전역 변수
    },
    parserOptions: {
      ecmaFeatures: {
        jsx: true,                                    // JSX 문법 지원
      },
    },
  },
  settings: {
    react: {
      version: 'detect',                              // React 버전 자동 감지
    },
  },
  rules: {
    // TypeScript 관련 규칙
    '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],  // 사용하지 않는 변수 에러 (_로 시작하는 건 제외)
    '@typescript-eslint/explicit-function-return-type': 'off',                   // 함수 리턴 타입 명시 강제 안함
    '@typescript-eslint/no-explicit-any': 'warn',                                // any 타입 사용 시 경고
    
    // 일반 JavaScript 규칙
    'no-console': 'warn',                             // console.log 사용 시 경고
    'no-debugger': 'error',                          // debugger 사용 시 에러
    'prefer-const': 'error',                         // 재할당 없으면 const 사용 강제
    'no-var': 'error',                               // var 사용 금지
  },
}
```
- **목적**: TypeScript 코드의 품질과 일관성 보장
- **JavaScript 규칙 포함 이유**: TypeScript는 JavaScript의 상위 집합이므로 JavaScript 안티패턴(var 사용, console.log, debugger 등)도 방지 필요

#### React 컴포넌트 규칙
```javascript
{
  files: ['**/*.{jsx,tsx}'],                         // React 컴포넌트 파일만
  plugins: {
    react,                                            // React 규칙
    'react-hooks': reactHooks,                        // React Hooks 규칙
    'react-refresh': reactRefresh,                    // Vite HMR 규칙
    'jsx-a11y': jsxA11y,                            // 접근성 규칙
  },
  rules: {
    ...reactHooks.configs.recommended.rules,          // React Hooks 권장 규칙 모두 적용
    
    'react-refresh/only-export-components': [         // Vite HMR을 위한 규칙
      'warn',
      { allowConstantExport: true },                  // 상수 export 허용
    ],
    
    // React 규칙
    'react/prop-types': 'off',                        // TypeScript 사용시 PropTypes 불필요
    'react/react-in-jsx-scope': 'off',                // React 17+ 자동 import
    
    // 접근성 규칙 (중요한 것들만)
    'jsx-a11y/alt-text': 'error',                    // 이미지 alt 속성 필수
    'jsx-a11y/anchor-has-content': 'error',          // 앵커 태그 내용 필수
    'jsx-a11y/anchor-is-valid': 'error',             // 유효한 앵커 태그
    'jsx-a11y/click-events-have-key-events': 'warn', // 클릭 이벤트에 키보드 이벤트도 추가
  },
}
```
- **목적**: React 컴포넌트의 품질과 접근성 보장

#### 테스트 파일 규칙 완화
```javascript
{
  files: ['**/*.{test,spec}.{js,ts,jsx,tsx}', '**/test/**/*.{js,ts,jsx,tsx}'],
  languageOptions: {
    globals: {
      ...globals.jest,                                // Jest 전역 변수
      ...globals.node,                                // Node.js 전역 변수
    },
  },
  rules: {
    '@typescript-eslint/no-explicit-any': 'off',     // 테스트에서는 any 허용
    'no-console': 'off',                              // 테스트에서는 console.log 허용
  },
}
```
- **목적**: 테스트 코드에서는 더 유연한 규칙 적용

### .prettierrc.json 설정 상세 분석

```json
{
  "semi": true,                                       // 세미콜론 항상 사용 (const x = 1;)
  "trailingComma": "all",                            // 모든 곳에 후행 쉼표 (함수 매개변수, 객체, 배열 등)
  "singleQuote": true,                               // 작은따옴표 사용 ('hello')
  "printWidth": 80,                                  // 한 줄 최대 80자
  "tabWidth": 2,                                     // 들여쓰기 2칸
  "useTabs": false,                                  // 탭 대신 공백 사용
  "quoteProps": "as-needed",                         // 필요시에만 객체 키에 따옴표
  "bracketSpacing": true,                            // 중괄호 내부 공백 ({ a: 1 })
  "bracketSameLine": false,                          // JSX 닫는 > 다음 줄에 배치
  "arrowParens": "avoid",                            // 매개변수 1개일 때 괄호 생략 (x => x + 1)
  "endOfLine": "lf",                                 // Unix 스타일 줄바꿈
  "embeddedLanguageFormatting": "auto",              // 임베디드 언어 자동 포매팅
  "htmlWhitespaceSensitivity": "css",                // HTML 공백 CSS 기본값 따름
  "insertPragma": false,                             // @format 주석 자동 삽입 안함
  "jsxSingleQuote": true,                            // JSX에서도 작은따옴표
  "proseWrap": "preserve",                           // Markdown 등에서 줄바꿈 유지
  "requirePragma": false,                            // @format 주석 없어도 포매팅
  "vueIndentScriptAndStyle": false                   // Vue 관련 설정 (사용 안함)
}
```
- **목적**: 일관된 코드 포매팅으로 코드 리뷰 시 스타일 논쟁 방지

### vite.config.ts 테스트 커버리지 설정

```typescript
test: {
  globals: true,                                      // 전역 테스트 함수 사용 (describe, it 등)
  environment: 'jsdom',                               // DOM 환경 시뮬레이션
  setupFiles: ['./src/test/setup.ts'],               // 테스트 설정 파일
  coverage: {
    provider: 'v8',                                   // V8 커버리지 엔진 사용
    reporter: ['text', 'json', 'json-summary', 'html'], // 다양한 형식 리포트
    reportsDirectory: './coverage',                   // 리포트 저장 디렉토리
    thresholds: {
      global: {
        branches: 70,                                 // 분기 커버리지 70%
        functions: 70,                                // 함수 커버리지 70%
        lines: 70,                                    // 라인 커버리지 70%
        statements: 70                                // 구문 커버리지 70%
      }
    },
    exclude: [                                        // 커버리지 제외 파일
      'node_modules/',
      'src/test/',
      '**/*.stories.{ts,tsx}',                        // Storybook 파일
      '**/*.test.{ts,tsx}',                          // 테스트 파일
      '**/*.spec.{ts,tsx}',
      'src/vite-env.d.ts',                           // 타입 정의 파일
      'src/main.tsx',                                // 앱 진입점
      '.storybook/',
      'dist/',
      'coverage/'
    ]
  }
}
```
- **목적**: 70% 최소 커버리지 강제로 코드 품질 보장

---

## 🚀 GitHub Actions CI/CD

### 백엔드 CI (ci.yml) 워크플로우

#### 트리거 설정
```yaml
on:
  push:
    branches: [ main, develop ]                       # main, develop 브랜치 push 시
    paths: 
      - 'studytrack-backend/**'                       # 백엔드 코드 변경시만
      - '.github/workflows/ci.yml'                    # 이 워크플로우 변경시
  pull_request:
    branches: [ main, develop ]                       # main, develop로의 PR 시
    paths: 
      - 'studytrack-backend/**'
      - '.github/workflows/ci.yml'
```
- **목적**: 불필요한 CI 실행 방지로 비용 절약 및 속도 향상

#### 작업 디렉토리 설정
```yaml
defaults:
  run:
    working-directory: studytrack-backend             # 모든 명령어를 백엔드 디렉토리에서 실행
```
- **목적**: 모노레포 환경에서 백엔드 작업만 수행

#### 권한 설정
```yaml
permissions:
  contents: read                                      # 코드 읽기 권한
  pull-requests: write                                # PR 댓글 작성 권한 (커버리지 리포트용)
  checks: write                                       # 체크 결과 작성 권한
```
- **목적**: 최소 권한 원칙으로 보안 강화

#### 실행 단계 분석

1. **소스코드 체크아웃**
   ```yaml
   - uses: actions/checkout@v4                       # 최신 안정 버전 사용
   ```

2. **Java 21 설정**
   ```yaml
   - name: Set up JDK 21
     uses: actions/setup-java@v4
     with:
       distribution: 'temurin'                        # Eclipse Temurin 배포판
       java-version: '21'                             # Java 21 사용
   ```

3. **Gradle 캐시 설정**
   ```yaml
   - name: Cache Gradle packages
     uses: actions/cache@v4
     with:
       path: |
         ~/.gradle/caches                             # Gradle 의존성 캐시
         ~/.gradle/wrapper                            # Gradle Wrapper 캐시
       key: ${{ runner.os }}-gradle-${{ hashFiles('studytrack-backend/**/*.gradle', 'studytrack-backend/**/gradle-wrapper.properties') }}
   ```
   - **목적**: 빌드 시간 단축 (의존성 재다운로드 방지)

4. **정적 분석 도구 실행**
   ```yaml
   - name: Run Checkstyle                             # 코드 스타일 검사
     run: ./gradlew checkstyleMain checkstyleTest
   
   - name: Run SpotBugs                               # 버그 탐지
     run: ./gradlew spotbugsMain spotbugsTest
   
   - name: Run PMD                                    # 코드 품질 분석
     run: ./gradlew pmdMain pmdTest
   ```
   - **순서**: 빠른 검사부터 실행하여 조기 실패 유도

5. **테스트 및 커버리지**
   ```yaml
   - name: Run Tests with Coverage                    # 테스트 실행 및 커버리지 측정
     run: ./gradlew test jacocoTestReport
   
   - name: Verify Coverage                            # 커버리지 기준 검증
     run: ./gradlew jacocoTestCoverageVerification
   ```
   - **목적**: 70% 최소 커버리지 강제

6. **리포트 업로드**
   ```yaml
   - name: Upload Test Results
     uses: actions/upload-artifact@v4
     if: always()                                     # 실패해도 업로드
     with:
       name: test-results
       path: studytrack-backend/build/reports/tests/
   ```
   - **목적**: 실패 원인 분석을 위한 상세 리포트 보존

### 프론트엔드 CI (frontend-ci.yml) 워크플로우

#### Node.js 설정
```yaml
- name: Setup Node.js
  uses: actions/setup-node@v4
  with:
    node-version: '20'                                # Node.js 20 LTS 사용
    cache: 'npm'                                      # npm 캐시 활성화
    cache-dependency-path: studytrack-front/package-lock.json
```
- **목적**: 안정적인 Node.js 환경 구성 및 의존성 캐시

#### 의존성 설치
```yaml
- name: Install dependencies
  run: npm ci                                         # package-lock.json 기반 정확한 설치
```
- **목적**: `npm install` 대신 `npm ci` 사용으로 재현 가능한 빌드

#### 정적 분석 실행
```yaml
- name: Run ESLint                                    # 코드 품질 검사
  run: npm run lint

- name: Run Prettier check                            # 포매팅 검사
  run: npx prettier --check .

- name: Run type check                                # TypeScript 타입 체크
  run: npx tsc --noEmit
```
- **순서**: 빠른 검사부터 실행

#### 테스트 및 빌드
```yaml
- name: Run tests with coverage                       # 테스트 및 커버리지
  run: npm run test:coverage

- name: Build application                             # 프로덕션 빌드
  run: npm run build
```
- **목적**: 배포 가능성 검증

---

## 📊 설정 파일별 상세 분석

### Checkstyle 규칙 (config/checkstyle/checkstyle.xml)

#### 파일 수준 검사
```xml
<module name="FileLength">
  <property name="max" value="2000"/>                 <!-- 파일 최대 2000줄 -->
</module>

<module name="LineLength">
  <property name="max" value="120"/>                  <!-- 라인 최대 120자 -->
  <property name="ignorePattern" value="^package.*|^import.*|a href|href|http://|https://|ftp://"/> <!-- URL 등 예외 -->
</module>

<module name="FileTabCharacter"/>                     <!-- 탭 문자 금지 -->
```
- **목적**: 파일 크기와 라인 길이 제한으로 가독성 향상

#### 네이밍 규칙
```xml
<module name="ConstantName"/>                         <!-- 상수명: UPPER_SNAKE_CASE -->
<module name="LocalVariableName"/>                    <!-- 지역변수: camelCase -->
<module name="MemberName"/>                           <!-- 멤버변수: camelCase -->
<module name="MethodName"/>                           <!-- 메서드명: camelCase -->
<module name="PackageName"/>                          <!-- 패키지명: lowercase -->
<module name="TypeName"/>                             <!-- 클래스명: PascalCase -->
```
- **목적**: Java 네이밍 컨벤션 강제

#### Import 규칙
```xml
<module name="AvoidStarImport"/>                      <!-- * import 금지 -->
<module name="IllegalImport"/>                        <!-- 금지된 패키지 import 차단 -->
<module name="RedundantImport"/>                      <!-- 중복 import 금지 -->
<module name="UnusedImports"/>                        <!-- 사용하지 않는 import 금지 -->
```
- **목적**: 깔끔한 import 구조 유지

### PMD 규칙 (config/pmd/pmd-rules.xml)

#### 베스트 프랙티스
```xml
<rule ref="category/java/bestpractices.xml">
  <exclude name="JUnitTestContainsTooManyAsserts"/>   <!-- JUnit 테스트의 assert 개수 제한 완화 -->
  <exclude name="JUnitAssertionsShouldIncludeMessage"/> <!-- Assert 메시지 강제 완화 -->
</rule>
```
- **목적**: 일반적인 베스트 프랙티스 적용하되 테스트 코드는 유연하게

#### 코드 스타일
```xml
<rule ref="category/java/codestyle.xml">
  <exclude name="OnlyOneReturn"/>                     <!-- 메서드당 return 1개 제한 완화 -->
  <exclude name="AtLeastOneConstructor"/>             <!-- 생성자 필수 완화 -->
  <exclude name="ShortVariable"/>                     <!-- 짧은 변수명 허용 -->
  <exclude name="LongVariable"/>                      <!-- 긴 변수명 허용 -->
</rule>
```
- **목적**: 과도하게 엄격한 규칙 완화

#### 설계 규칙
```xml
<rule ref="category/java/design.xml">
  <exclude name="LawOfDemeter"/>                      <!-- 디미터 법칙 완화 (Spring에서 흔한 패턴) -->
  <exclude name="LoosePackageCoupling"/>              <!-- 패키지 결합도 완화 -->
  <exclude name="UseUtilityClass"/>                   <!-- 유틸리티 클래스 강제 완화 -->
</rule>
```
- **목적**: Spring Boot 프로젝트 특성에 맞게 조정

#### 커스텀 임계값
```xml
<rule ref="category/java/design.xml/ExcessiveMethodLength">
  <properties>
    <property name="minimum" value="100"/>            <!-- 메서드 최대 100줄 -->
  </properties>
</rule>

<rule ref="category/java/design.xml/CyclomaticComplexity">
  <properties>
    <property name="methodReportLevel" value="10"/>   <!-- 메서드 복잡도 10 이하 -->
    <property name="classReportLevel" value="40"/>    <!-- 클래스 복잡도 40 이하 -->
  </properties>
</rule>
```
- **목적**: 프로젝트 규모에 맞는 적절한 임계값 설정

---

## 🎯 설정의 전체적인 목적과 효과

### 1. 코드 품질 보장
- **일관된 스타일**: 모든 개발자가 동일한 코딩 스타일 사용
- **버그 예방**: 정적 분석으로 잠재적 문제 조기 발견
- **가독성 향상**: 표준화된 포매팅과 네이밍

### 2. 개발 생산성 향상
- **자동화**: CI/CD로 수동 검사 과정 제거
- **빠른 피드백**: PR 단계에서 즉시 문제 발견
- **캐시 활용**: 빌드 시간 단축

### 3. 팀 협업 개선
- **코드 리뷰 품질**: 스타일 논쟁 없이 로직에 집중
- **온보딩 용이**: 새 팀원도 일관된 코드 작성 가능
- **지식 공유**: 표준화된 패턴과 규칙

### 4. 유지보수성 확보
- **테스트 커버리지**: 70% 이상 유지로 안정성 보장
- **문서화**: 설정 파일 주석으로 의도 명확화
- **확장성**: 새로운 도구 추가 용이

이 모든 설정은 **코드 품질 향상**과 **개발 효율성 증대**를 위해 신중하게 선택되고 조정되었습니다. 🚀
