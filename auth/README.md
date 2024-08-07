## 🍀 HarpSharp_SERVER_Auth
Jwt 기반 인증/인가 서버입니다. 이외에도 회원가입/로그인/로그아웃을 처리합니다.

## 구성도
![배포 구성도 drawio (2)](https://github.com/user-attachments/assets/c2697fe1-96ae-4a6e-bb92-9a34cf6cff1d)


## API
![스크린샷 2024-07-27 121332](https://github.com/user-attachments/assets/127aa618-8af1-43b7-8238-d837b16c154f)


### 💣 트러블 슈팅

1. ✅ 배포 시 EC2 CPU 로드율 100%로 서버 다운
    1.  `t2.micro`에서 `t3.small`로 인스턴스 변경
2. ✅ 기존 로컬/테스트 환경 설정(`application.yml`) 으로 EC2 환경에서 구동 실패
    1.   `applicaiton.yml`을 `테스트용`과 `배포용` 으로 분리
    2. github actions && 로컬 테스트 → `application.yml`
    3. EC2 → `application-prod.yml`
3. ✅ 개별 커맨드로 컨테이너를 띄우니 spring boot ↔ DB 간 통신이 어려움 
    1.  `docker-compose` 로 도커라이즈 환경 통합
    2. `networks` 옵션 추가
4. ✅ EC2 용량 부족으로 docker image 생성 불가..
    1. 필요없는 파일 제거 + (볼륨 확장..도 고려 중)
    2. 8Gb → 30Gb로 확장
    3. 총 비용 = 약 3만원/month
5. ✅ EC2 외부 퍼블릭 IP로 접근 불가
    1. 어제 오후 시간대에 보안그룹을 수정했으나 증상 여전 → 재설정 후 해결
        1. ICMP, ICMPv6 전체 허용, TCP 80, 443, 3000, 8000, 8080 허용
6. ⚠️ 외부 퍼블릭 IP로 POST 요청을 보낼 경우 쿠키 생성 X
    1. 크롬 브라우저 정책 상 Https 요청이 아니면 setCookie가 동작하지 않음..
    2. Https를 적용해 해결할 예정 ( DNS 고려 중)
