# PostgreSQL Database Setup (Docker 기반)

## ✅ 사용 목적

이 프로젝트에서 사용할 데이터베이스는 **PostgreSQL**이다.  
운영과 개발 환경을 분리하고, 버전 및 환경에 독립적인 DB 실행을 위해 **Docker** 컨테이너 기반 구성을 채택한다.

---

## 1. ✅ 선택한 DB: PostgreSQL

### 📌 선택 이유

- MySQL과 비교해도 더 강력한 JSON 지원 및 함수형 확장성
- 오픈소스 RDBMS 중 가장 활발한 개발 커뮤니티
- 타 DB보다 복잡한 쿼리 처리 및 서브쿼리에 유리
- TypeORM, Prisma, Django ORM 등과 호환성 우수

---

## 2. 🐳 Docker 기반 구성

### ⚙️ 도입 이유

| 항목 | 로컬 설치 | Docker 설치 (우리가 선택한 방식) |
|------|-----------|--------------------------|
| 설치 위치 | 로컬 OS에 직접 설치 | Docker 컨테이너 내부 |
| 환경 분리 | 어려움 | 완벽한 개발/운영 환경 격리 |
| 이식성 | 낮음 | `.yml` 하나로 완벽 복제 가능 |
| 삭제/초기화 | 복잡 | `docker compose down -v`로 간단 |
| 협업 | 설정 공유 어려움 | 환경 전체 버전 관리 가능 |

### 🧱 docker-compose 구조 (예정)

```yaml
version: '3.8'
services:
  db:
    image: postgres:16
    container_name: my_pg_db
    environment:
      POSTGRES_USER: your_user
      POSTGRES_PASSWORD: your_pass
      POSTGRES_DB: your_db
    volumes:
      - ./pgdata:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - backend_network

```
### 추후 진행 예정 작업
- docker-compose.yml 정식 작성
- .env 파일 분리로 DB 정보 관리
- 백엔드와 PostgreSQL 연결 설정
- ERD 설계 완료 후 마이그레이션 스크립트 작성

