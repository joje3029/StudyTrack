// StudyTrack Frontend ESLint 설정
// - React + TypeScript + Vite 프로젝트용
// - Flat Config 형식 (ESLint 9.x)
// - 접근성, React Hooks, 코드 품질 규칙 포함

import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import react from 'eslint-plugin-react'
import jsxA11y from 'eslint-plugin-jsx-a11y'
import tseslint from 'typescript-eslint'

export default tseslint.config([
  // 전역 무시 패턴
  {
    ignores: [
      'dist/**',
      'build/**',
      'node_modules/**',
      'coverage/**',
      '.storybook/**',
      'storybook-static/**',
      '*.config.js',
      '*.config.ts',
      'vite.config.ts'
    ]
  },
  
  // JavaScript/TypeScript 파일용 기본 설정
  {
    files: ['**/*.{js,mjs,cjs,ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2022,
      globals: {
        ...globals.browser,
        ...globals.es2022,
      },
      parserOptions: {
        ecmaFeatures: {
          jsx: true,
        },
      },
    },
    settings: {
      react: {
        version: 'detect',
      },
    },
    extends: [
      js.configs.recommended,
      ...tseslint.configs.recommended,
    ],
    rules: {
      // TypeScript 관련 규칙
      '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
      '@typescript-eslint/explicit-function-return-type': 'off',
      '@typescript-eslint/explicit-module-boundary-types': 'off',
      '@typescript-eslint/no-explicit-any': 'warn',
      
      // 일반 JavaScript 규칙
      'no-console': 'warn',
      'no-debugger': 'error',
      'prefer-const': 'error',
      'no-var': 'error',
    },
  },
  
  // React 컴포넌트용 설정
  {
    files: ['**/*.{jsx,tsx}'],
    plugins: {
      react,
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      'jsx-a11y': jsxA11y,
    },
    extends: [
      react.configs.flat.recommended,
      react.configs.flat['jsx-runtime'],
    ],
    rules: {
      // React Hooks 규칙
      ...reactHooks.configs.recommended.rules,
      
      // React Refresh 규칙 (Vite HMR용)
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      
      // React 규칙
      'react/prop-types': 'off', // TypeScript 사용시 불필요
      'react/react-in-jsx-scope': 'off', // React 17+ 자동 import
      'react/jsx-uses-react': 'off',
      
      // 접근성 규칙 (중요한 것들만)
      'jsx-a11y/alt-text': 'error',
      'jsx-a11y/anchor-has-content': 'error',
      'jsx-a11y/anchor-is-valid': 'error',
      'jsx-a11y/click-events-have-key-events': 'warn',
      'jsx-a11y/no-static-element-interactions': 'warn',
    },
  },
  
  // 테스트 파일용 설정
  {
    files: ['**/*.{test,spec}.{js,ts,jsx,tsx}', '**/test/**/*.{js,ts,jsx,tsx}'],
    languageOptions: {
      globals: {
        ...globals.jest,
        ...globals.node,
      },
    },
    rules: {
      // 테스트에서는 더 관대한 규칙
      '@typescript-eslint/no-explicit-any': 'off',
      'no-console': 'off',
    },
  },
  
  // Storybook 파일용 설정
  {
    files: ['**/*.stories.{js,ts,jsx,tsx}'],
    rules: {
      // Storybook에서는 export default를 많이 사용
      'react-refresh/only-export-components': 'off',
    },
  },
])
