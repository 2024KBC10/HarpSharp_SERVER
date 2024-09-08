# openai_api.py

import openai, aiohttp, os, asyncio
from io import BytesIO
from PIL import Image
from googletrans import Translator
from fastapi import HTTPException
from dotenv import load_dotenv
from history_service import HistoryService

load_dotenv()
openai.api_key = os.getenv("OPENAI_API_KEY")

translator = Translator()

# MongoDB URL 설정
db_url = os.getenv("MONGO_DB_URL", "mongodb://root:mongo123!@mongo:27017") # localhost -> harpsharp.com 으로 교체
history_service = HistoryService(db_url)

# 비동기 번역 함수
async def translate_text(kr_prompt):
    loop = asyncio.get_event_loop()
    translated = await loop.run_in_executor(None, translator.translate, kr_prompt, 'en')
    return translated.text

# 이미지 생성 관련 함수
async def fetch_image(session, url):
    async with session.get(url) as response:
        if response.status == 200:
            return await response.read()
        else:
            raise HTTPException(status_code=500, detail=f"Failed to download image, status code: {response.status}")

async def generate_image_from_text(username, kr_prompt):
    translated_prompt = await translate_text(kr_prompt)
    
    try:
        response = await openai.Image.acreate(
            prompt=translated_prompt,
            n=1,
            size='256x256',
            model='dall-e-2'
        )

        image_url = response['data'][0]['url']
        # 히스토리 저장 (질문과 이미지 URL을 저장)
        history_service.save_history(username, kr_prompt, image_url)

        return image_url
    except Exception as e:
        print(f"An error occurred: {e}")
        raise HTTPException(status_code=500, detail="Image generation failed")

# 텍스트 생성 관련 함수
async def generate_text(username, prompt, model_name='gpt-4o-mini'):
    # 사용자 히스토리 가져오기
    history = history_service.get_history(username)

    # 이전 대화 기록을 바탕으로 대화 문맥을 유지하는 프롬프트 생성
    context = ""
    for entry in history[-5:]:  # 최근 5개의 대화만 가져와 문맥 생성 -> 범위는 조정하면 됨 
        context += f"User: {entry['question']}\nAssistant: {entry['answer']}\n"

    # 새로운 질문과 결합
    full_prompt = f"{context}User: {prompt}\nAssistant:"

    response = await openai.ChatCompletion.acreate(
        model=model_name,
        messages=[
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": full_prompt}
        ],
        max_tokens=500 # 최대 답변 생성 길이 
    )
    generated_text = response['choices'][0]['message']['content']

    # 히스토리 저장 (질문과 생성된 텍스트 저장)
    history_service.save_history(username, prompt, generated_text)

    return generated_text

# 응답 타입 감지 함수
def detect_response_type(prompt):
    image_keywords = ['그림', '이미지', '사진', '그려줘', '그려', '그림으로', '사진으로', '이미지로']
    if any(keyword in prompt for keyword in image_keywords):
        return 'image'
    else:
        return 'text'
