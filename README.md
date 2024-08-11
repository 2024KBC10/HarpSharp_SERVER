### 🍀 HarpSharp_SERVER
HarpSharp backend입니다. <br>
HarpSharp는 백오피스를 지향하는 서비스로 <br>
클라우드 네이티브 환경에서 도커 기반의 MSA 방식으로 구현되었습니다.<br>

<br>

인증/인가를 담당하는 Auth, <br>
게시글, 댓글 CRUD 기능을 가진 Board/TODO <br>
ChatGPT API를 연결한 생성형 AI 서버로 구성되어 있습니다. <br>
 
 <br>
 
현재 https://harpsharp.com 도메인으로 접근 가능하며
AWS EC2에서 개별 컨테이너로 배포 중입니다. <br>

 <br>

### 기술 스택
#### CI/CD
![Github actions](https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=GithubActions&logoColor=white) ![S3](https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=S3&logoColor=whit) ![CodeDeploy](https://img.shields.io/badge/CodeDeploy-569A31?style=for-the-badge) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white) ![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=Nginx&logoColor=white) <br>


#### API 서버
Auth, Board, TODO: [JAVA17](https://img.shields.io/badge/JAVA17-6DB33F?style=for-the-badge), ![Spring Boot 3.3.1](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white) [Spring Security](https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white) [JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge) <br>

AI: python 3.14.2, Flask <br>
#### 운용 환경
HW: EC2 t3.medium <br>
OS: ubuntu <br>
#### DB
통합 DB: mySQL 8.0 <br>
Refresh Token: Redis <br>
#### 빌드
JAVA: gradle-8.5+
<br>
#### 문서화 및 협업
API 명세서: [Swagger](http://harpsharp.com:9000/) <br>
협업 툴: github, [notion](https://www.notion.so/aa19bf4f9e87408391e2b9d29fb3a2dd), slack <br>


### 구성도
![배포 구성도 drawio (3) (1) drawio](https://github.com/user-attachments/assets/5d9c108f-9359-49c2-88e3-f2c99b6618ea)

각 기술
