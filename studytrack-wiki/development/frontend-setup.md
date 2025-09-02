# 프론트엔드 개발 환경 셋업 가이드

이 문서는 `studytrack-front` 프로젝트의 개발 환경을 설정하는 전체 과정을 안내합니다.

---

## 1. 프로젝트 생성 및 기본 셋업

```bash
# Vite + React + TypeScript 템플릿 생성
npm create vite@latest studytrack-front -- --template react-ts
cd studytrack-front

# 의존성 설치
npm install
```

## 2. Tailwind CSS 설치 및 설정
```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

`tailwind.config.js`와 `postcss.config.js` 파일이 자동 생성됩니다.

`tailwind.config.js`의 `content` 부분을 다음과 같이 수정합니다.
```js
// tailwind.config.js
module.exports = {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: { extend: {} },
  plugins: [],
}
```

`src/index.css` 파일의 내용을 아래와 같이 수정하거나 추가합니다.
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

## 3. shadcn/ui 설치 및 설정
```bash
npx shadcn-ui@latest init
# 설치 과정에서 TypeScript, Tailwind CSS 등 프로젝트 설정에 맞게 옵션을 선택합니다.

# 예시: Button, Card 컴포넌트 추가
npx shadcn-ui@latest add button card
```

## 4. 상태 관리 및 서버 상태 라이브러리 설치
```bash
npm install zustand @tanstack/react-query
```

## 5. 테스트 도구 설치
```bash
npm install -D vitest @testing-library/react @testing-library/jest-dom jsdom
```

## 6. 코드 스타일 및 포맷팅 도구 설치
```bash
npm install -D eslint prettier eslint-config-prettier eslint-plugin-react eslint-plugin-react-hooks eslint-plugin-jsx-a11y

# ESLint 설정 파일 생성
npx eslint --init
```

## 7. Storybook 설치
```bash
npx storybook@latest init
```
설치 과정에서 `react-ts`를 선택하고 필요한 패키지를 설치합니다.

## 8. (선택) E2E 테스트 도구 설치
```bash
npm install -D cypress
npx cypress open
```

## 9. (권장) Git Hooks 설정 (Husky + lint-staged)
커밋 또는 푸시 전에 코드 품질을 자동으로 검사합니다.
```bash
npm install -D husky lint-staged
npx husky install
npx husky add .husky/pre-commit "npx lint-staged"
```

`lint-staged.config.mjs` 파일을 생성하고 아래와 같이 설정합니다.
```js
export default {
  "**/*.{ts,tsx}": ["eslint --fix", "prettier --write"]
}
```
