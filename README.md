### 🍀 HarpSharp_SERVER
<img src = "https://github.com/user-attachments/assets/e017dfcb-d65d-4e78-a607-9444707db272" width = "30%" height = "30%">

<br></br>

**HarpSharp backend**입니다. <br>
**HarpSharp**는 백오피스를 지향하는 서비스로 <br>
클라우드 네이티브 환경에서 도커 기반의 MSA 방식으로 구현되었습니다.<br>

<br>

인증/인가를 담당하는 **Auth** <br>
게시글, 댓글 CRUD 기능을 가진 **Board/TODO** <br>
ChatGPT API를 연결한 생성형 AI 서버로 구성되어 있습니다. <br>
<br> 
현재 https://harpsharp.com 도메인으로 접근 가능하며 <br>
AWS EC2에서 개별 컨테이너로 배포 중입니다. <br>
<br>

### 🛠️ 기술 스택

| 목적 | 사용 기술 |
|:---:|:----:|
|CI/CD|![Github actions](https://img.shields.io/badge/Actions-2088FF?style=for-the-badge&logo=GithubActions&logoColor=white) ![S3](https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=AmazoneS3&logoColor=whit) ![CodeDeploy](https://img.shields.io/badge/CodeDeploy-ab0fd7?style=for-the-badge) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white) ![NGINX](https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=Nginx&logoColor=white)|
|API 서버|![JAVA17](https://img.shields.io/badge/JAVA17-6DB33F?style=for-the-badge) ![Spring Boot 3.3.1](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white) ![Spring Security](https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge) ![python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white) ![Flask](https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=Flask&logoColor=white) |
| 운용 환경 | ![EC2 t3.medium](https://img.shields.io/badge/t3.midium-FF9900?style=for-the-badge&logo=EC2&logoColor=white) ![ubuntu](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white) |
| DB | ![mySQL 8.0](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white) |
| 빌드 | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white) |
| 협업 | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white) ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white) ![notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white) ![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white) |
| 테스트 | ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white) ![MockMVC](https://img.shields.io/badge/MockMVC-f2d418?style=for-the-badge)|


<br>

### 🗺️ 시스템 아키텍처
<img src = "https://github.com/user-attachments/assets/5d9c108f-9359-49c2-88e3-f2c99b6618ea" width = "80%" height = "80%">

<br>

### 배포 절차
1. main or dev 브랜치로 push 혹은 pull request가 발생하면 Actions CI 스크립트에 따라 빌드 및 테스트가 진행됩니다. 
2. 배포 스크립트, appspec.yml, docker.yml, *.jar 등 배포에 필요한 파일들을 zip으로 압축해 S3에 업로드 합니다. 
3. CodeDeploy에 배포 요청을 전달, 트리거가 발동돼 S3 버킷에 업로드 된 deploy.zip 파일을 받아 스크립트에 따라 배포를 시작합니다.
4. 스크립트는 현재 배포 전 서비스의 컨테이너를 내린 후, 배포 폴더 내부의 docker-compose를 실행 시킵니다.
5. 올라간 컨테이너는 도커 bridge 네트워크에 합류하며, 내부 컨테이너 간 HTTP 통신을 통해 API를 주고 받습니다.

<br></br>

### 서버 구성
![서버 구성 drawio](https://github.com/user-attachments/assets/cf546632-6b30-4172-94d5-02e22ba77bb3)

#### 리버스 프록시 서버 (NGINX)
- 서버의 앞단에 위치하며 리버스 프록시를 수행합니다.
- 클라이언트의 API를 올바른 엔드포인트로 전달합니다.
- 인증/인가가 필요한 요청의 경우 Auth 서버로 전달합니다.
- 인증/인가를 받은 API를 기존 엔드포인트로 전달합니다.
#### 인증/인가 서버 (Auth)
- 헤더에 포함된 JWT의 유효성 검증 및 인증/인가를 수행합니다.
- 유효한 요청의 경우 Nginx으로 검증이 완료되었다는 응답을 보냅니다. (200 OK)
#### 도메인 서버 (Board, Todo, AI)
- 도메인 로직을 수행합니다.
- CRUD와 ChatGPT API 요청 및 응답을 수행합니다. 
#### DB (mySQL, Redis)
- 인증/인가 서버와 도메인 서버 모두가 공유하는 DB 입니다.
- RDB에선 유저 정보, Posts, Comments와 같은 실질적인 리소스를 저장합니다.
- Redis의 경우 Refresh Token을 저장하며 Auth 서버에서 JWT 유효성을 검증하는 로직에 활용됩니다.


### 🧱 ERD
<img src = "https://github.com/user-attachments/assets/776a5064-9923-41b4-87ac-86012cf94628" width = "50%" height = "25%">  

<br>

### 🍏 주요 기능

#### JWT 인증/인가
#### 발급과정
<img src = "https://github.com/user-attachments/assets/3ea2d271-35b0-40a1-8763-629b721c4c9f" width = "30%" height = "30%">
1. 클라이언트가 로그인을 요청합니다.
2. 스프링 시큐리티에서 회원 DB를 확인해 해당 클라이언트가 가입된 회원인지를 검증합니다.
3. 회원의 식별자 + username + role을 조합해 Access Token과 Refresh Token을 발급합니다.
4. Refresh Token은 차후 Access Token 검증 과정에 활용되며, Redis에 저장합니다. (Key: Access Token, Value: RefreshEntity)
5. HTTP 통신을 통해 Access Token은 헤더에 담고, Refresh Token은 쿠키에 담아 클라이언트에게 전달합니다.

<br>

#### 검증 과정
<img src = "https://github.com/user-attachments/assets/76c171bc-5ab2-4471-b142-6b48105497a4" width = "30%" height = "30%">
1. 클라이언트가 헤더에 Access Token을 담아 NGINX(프록시 서버)로 Request를 전달합니다.
2. Request가 인가가 필요한 API인 경우, Auth 서버로 해당 Request를 전달합니다.
3. Request에 담긴 Access Token을 꺼내 유효성을 검증합니다.
4. Redis 내부에 Access Token을 키로 가진 Refresh Entity가 존재하는 지 확인합니다.
5. Access Token과 Refresh Token의 유효성 여부를 NGINX로 전달합니다.
6. 유효성이 검증된 경우, 기존 도메인 엔드포인트로 해당 요청을 전달합니다.

#### 리버스 프록시 및 API 라우팅

#### Swagger + RestDocs를 활용한 API 명세서 자동화

#### CRUD

<br>

#### 💣 트러블 슈팅

1. CI/CD 구축
2. 스프링 시큐리티 필터체인 Exception Handling
3. NGINX 커스텀
4. JPA 연관 관계 매핑과 영속성 컨택스트
5. Swagger 서버 통합까지의 시행 착오
6. Response 컨벤션 통합


<br>

### 🕰️ 진행 현황
#### 서버 배포 및 포스트맨 기반 API 테스트 
2024.07.17 ~ 2024.08.11
#### 프론트 연결
2024.08.10 ~ 

<br>

#### 🤝 협업 링크
[notion](https://www.notion.so/aa19bf4f9e87408391e2b9d29fb3a2dd) <br>
[swagger](http://harpsharp.com:9000/) <br>
