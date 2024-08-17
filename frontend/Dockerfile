# 1. Node.js 이미지를 베이스로 사용합니다.
FROM node:18-alpine

# 2. 작업 디렉토리를 설정합니다.
WORKDIR /app

# 3. package.json과 package-lock.json을 복사합니다.
COPY package*.json ./

# 4. 의존성을 설치합니다.
RUN npm install

# 5. Next.js 프로젝트 파일을 복사합니다.
COPY . .

# 6. Next.js 애플리케이션을 빌드합니다.
RUN npm run build

# 7. Next.js 애플리케이션을 실행합니다.
ENTRYPOINT ["npm", "start"]

# 8. 애플리케이션이 3000 포트에서 동작하도록 설정합니다.
EXPOSE 3000