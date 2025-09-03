# ⚛️ 프론트엔드 개발 환경 설정 가이드

이 문서는 `studytrack-front` 프로젝트의 개발 환경을 설정하는 전체 과정을 안내합니다.

---

### ✅ 사전 요구사항

- [Node.js](https://nodejs.org/ko) (LTS 버전 권장)
- [pnpm](https://pnpm.io/ko/installation) (npm 또는 yarn도 사용 가능하나, pnpm을 권장)

---

### 🚀 1단계: 프로젝트 생성 및 의존성 설치

1.  **Vite를 사용하여 React + TypeScript 프로젝트를 생성합니다.**
    ```bash
    pnpm create vite studytrack-front --template react-ts
    ```

2.  **프로젝트 디렉토리로 이동하여 의존성을 설치합니다.**
    ```bash
    cd studytrack-front
    pnpm install
    ```

---

### 🎨 2단계: 스타일링 도구 설정 (Tailwind CSS + shadcn/ui)

1.  **Tailwind CSS와 관련 도구를 설치합니다.**
    ```bash
    pnpm install -D tailwindcss postcss autoprefixer
    npx tailwindcss init -p
    ```

2.  **`tailwind.config.js` 파일을 프로젝트에 맞게 설정합니다.** (shadcn/ui가 이 부분을 자동화해줍니다.)

3.  **shadcn/ui를 초기화합니다.** 이 과정에서 Tailwind 설정이 자동으로 구성됩니다.
    ```bash
    npx shadcn-ui@latest init
    ```
    *CLI 질문에 프로젝트 설정(TypeScript, `tailwind.config.js` 경로 등)에 맞게 답변합니다.*

4.  **`src/index.css` 파일에 Tailwind 기본 지시어를 추가합니다.**
    ```css
    @tailwind base;
    @tailwind components;
    @tailwind utilities;
    ```

---

### ⚙️ 3단계: 핵심 라이브러리 설치

1.  **상태 관리 및 서버 상태 라이브러리를 설치합니다.**
    - `Zustand`: 가벼운 클라이언트 상태 관리
    - `TanStack Query`: 서버 상태(API 데이터) 관리, 캐싱, 동기화
    ```bash
    pnpm install zustand @tanstack/react-query
    ```

2.  **라우팅 라이브러리를 설치합니다.**
    ```bash
    pnpm install react-router-dom
    ```

---

### ✅ 4단계: 테스트 환경 설정

1.  **단위/통합 테스트 도구를 설치합니다.**
    - `Vitest`: 테스트 러너 및 프레임워크
    - `React Testing Library`: 사용자 관점의 컴포넌트 테스트 유틸리티
    ```bash
    pnpm install -D vitest @testing-library/react @testing-library/jest-dom jsdom
    ```

2.  **`vite.config.ts`에 테스트 설정을 추가합니다.**
    ```typescript
    /// <reference types="vitest" />
    import { defineConfig } from 'vite'
    import react from '@vitejs/plugin-react'

    export default defineConfig({
      plugins: [react()],
      test: {
        globals: true,
        environment: 'jsdom',
        setupFiles: './src/test/setup.ts',
      },
    })
    ```

---

### 💅 5단계: 코드 품질 및 컨벤션 설정

1.  **ESLint와 Prettier 관련 패키지를 설치합니다.**
    ```bash
    pnpm install -D eslint prettier eslint-config-prettier eslint-plugin-react eslint-plugin-react-hooks eslint-plugin-jsx-a11y
    ```

2.  **ESLint 설정을 초기화합니다.**
    ```bash
    npx eslint --init
    ```

3.  **(권장) Git Hooks를 설정하여 코드 품질을 자동화합니다.**
    - `Husky`: Git Hooks를 쉽게 관리
    - `lint-staged`: 커밋할 파일에 대해서만 린트 및 포맷팅 실행
    ```bash
    pnpm install -D husky lint-staged
    npx husky install
    npx husky add .husky/pre-commit "npx lint-staged"
    ```

---

### 📚 6단계: 컴포넌트 문서화 (Storybook)

1.  **Storybook을 프로젝트에 추가합니다.**
    ```bash
    npx storybook@latest init
    ```
    *설치 과정에서 프레임워크(`react-ts`)를 선택하고 필요한 패키지를 설치합니다.*

2.  **Storybook을 실행합니다.**
    ```bash
    pnpm run storybook
    ```
    *브라우저에서 `http://localhost:6006`으로 접속하여 UI를 확인합니다.*

### 📚 6단계: 컴포넌트 문서화 (Storybook)

1.  **Storybook을 프로젝트에 추가합니다.**
    ```bash
    npx storybook@latest init
    ```
    *설치 과정에서 프레임워크(`react-ts`)를 선택하고 필요한 패키지를 설치합니다.*

2.  **Storybook을 실행합니다.**
    ```bash
    pnpm run storybook
    ```
    *브라우저에서 `http://localhost:6006`으로 접속하여 UI를 확인합니다.*

3.  **(사용법) 스토리(Story) 작성 예시**
    - Storybook은 컴포넌트 단위의 UI 개발 및 테스트를 위해, `.stories.tsx` 확장자를 가진 파일에 컴포넌트의 각 상태를 정의합니다.
    - 예를 들어, `src/components/Button.tsx` 컴포넌트에 대한 스토리는 아래와 같이 작성할 수 있습니다.

    **`src/components/Button.stories.tsx`**
    ```typescript
    import type { Meta, StoryObj } from '@storybook/react';
    import { Button } from './Button'; // 실제 컴포넌트 import

    const meta: Meta<typeof Button> = {
      title: 'Example/Button', // Storybook 사이드바에 표시될 경로
      component: Button,
      tags: ['autodocs'], // 컴포넌트 props 자동 문서화
      argTypes: { // 각 props를 컨트롤하기 위한 설정
        backgroundColor: { control: 'color' },
      },
    };

    export default meta;
    type Story = StoryObj<typeof Button>;

    // "Primary" 상태의 스토리
    export const Primary: Story = {
      args: {
        primary: true,
        label: 'Button',
      },
    };

    // "Secondary" 상태의 스토리
    export const Secondary: Story = {
      args: {
        label: 'Button',
      },
    };
    ```
