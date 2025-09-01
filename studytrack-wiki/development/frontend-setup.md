# 제목 없음

# 프론트엔드 개발 환경 셋업 가이드

---

## 1. 프로젝트 생성 및 기본 셋업

```bash
# Vite + React + TypeScript 템플릿 생성
npm create vite@latest studytrack-front -- --template react-ts
cd studytrack-front

# 의존성 설치
npm install
2. Tailwind CSS 설치 및 설정
bash
복사
편집
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
npx tailwindcss init -p
→ tailwind.config.js와 postcss.config.js 파일을 자동 생성

만약 npx tailwindcss init -p가 안 되면 수동으로 아래 두 파일을 만들어 주세요.

수동 설정 방법
tailwind.config.js

js
복사
편집
module.exports = {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: { extend: {} },
  plugins: [],
}
postcss.config.js

js
복사
편집
module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
3. Tailwind CSS 사용을 위해 src/index.css 수정
css
복사
편집
@tailwind base;
@tailwind components;
@tailwind utilities;
그리고 main.tsx나 main.jsx에서 아래처럼 import 해주세요:

ts
복사
편집
import './index.css'
4. shadcn/ui 설치 및 설정
bash
복사
편집
npx shadcn-ui@latest init
# 설치 옵션에서 Tailwind 사용 여부, TypeScript 여부 선택

npx shadcn-ui@latest add button card
5. 상태 관리 및 서버 상태 라이브러리 설치
bash
복사
편집
npm install zustand @tanstack/react-query
6. 테스트 도구 설치 (Vitest, React Testing Library)
bash
복사
편집
npm install -D vitest @testing-library/react @testing-library/jest-dom jsdom
7. ESLint + Prettier 설치 및 초기화
bash
복사
편집
npm install -D eslint prettier eslint-config-prettier eslint-plugin-react eslint-plugin-react-hooks eslint-plugin-jsx-a11y
npx eslint --init
ESLint 초기화 시 React, TypeScript, Browser, JSON 기반 등 선택

8. Storybook 설치 및 사용법
bash
복사
편집
npx storybook@latest init
프레임워크 선택에서 react-ts 선택

필요한 패키지 설치 동의 시 Yes 선택

설치 완료 후 package.json에 다음 스크립트가 자동 추가됨

json
복사
편집
"scripts": {
  "storybook": "storybook dev -p 6006",
  "build-storybook": "storybook build"
}
Storybook이란?
UI 컴포넌트를 독립적으로 개발하고 문서화할 수 있는 도구

컴포넌트 단위로 시각적 테스트와 상태 변경 테스트 가능

실행 방법
bash
복사
편집
npm run storybook
브라우저에서 <http://localhost:6006> 으로 접속해 Storybook UI 확인 가능

9. Cypress 설치 및 사용법 (선택 사항)
bash
복사
편집
npm install -D cypress
npx cypress open
Cypress란?
실제 브라우저 환경에서 사용자 행동(클릭, 입력, 네비게이션 등)을 자동으로 테스트하는 E2E(End-to-End) 테스트 도구

전체 앱 흐름이 의도대로 작동하는지 검증 가능

실행 방법
bash
복사
편집
npx cypress open
Cypress 전용 UI가 열리고, 테스트를 작성하고 실행할 수 있음

10. Husky + lint-staged로 커밋/푸시 전 코드 검사 자동화
bash
복사
편집
npm install -D husky lint-staged
npx husky install
npx husky add .husky/pre-commit "npx lint-staged"
npx husky add .husky/pre-push "npm run lint"
lint-staged.config.mjs 예시

js
복사
편집
export default {
  "**/*.{ts,tsx}": ["eslint --fix", "prettier --write"]
}
정리
단계	명령어 및 설명
1	npm create vite@latest studytrack-front -- --template react-ts
2	npm install
3	npm install -D tailwindcss postcss autoprefixer + npx tailwindcss init -p 또는 수동 설정
4	src/index.css에 Tailwind 기본 지시어 추가 및 import './index.css'
5	npx shadcn-ui@latest init 및 컴포넌트 추가
6	npm install zustand @tanstack/react-query
7	npm install -D vitest @testing-library/react @testing-library/jest-dom jsdom
8	npm install -D eslint prettier ... + npx eslint --init
9	npx storybook@latest init 후 npm run storybook
10	(선택) npm install -D cypress + npx cypress open
11	npm install -D husky lint-staged 및 Husky 설정

```