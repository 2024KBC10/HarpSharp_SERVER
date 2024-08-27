# 베이스 이미지 선택
FROM python:3.12-slim

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 파일 복사
COPY . /app

# 필요 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# Flask 서버 실행
CMD ["python", "app.py"]
