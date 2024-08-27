import openai, aiohttp, os, asyncio
from io import BytesIO
from PIL import Image
from googletrans import Translator
from fastapi import HTTPException

from dotenv import load_dotenv

load_dotenv()
openai.api_key = os.getenv("OPENAI_API_KEY")


translator = Translator()

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

async def generate_image_from_text(kr_prompt):
    translated_prompt = await translate_text(kr_prompt)
    
    try:
        response = await openai.Image.acreate(
            prompt=translated_prompt,
            n=1,
            size='256x256',
            model='dall-e-2'
        )

        image_url = response['data'][0]['url']

        async with aiohttp.ClientSession() as session:
            image_data = await fetch_image(session, image_url)
            image = Image.open(BytesIO(image_data))
            return image
    except Exception as e:
        print(f"An error occurred: {e}")
        raise HTTPException(status_code=500, detail="Image generation failed")

# 텍스트 생성 관련 함수
async def generate_text(prompt, model_name='gpt-4o-mini'):
    response = await openai.ChatCompletion.acreate(
        model=model_name,
        messages=[
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": prompt}
        ],
        max_tokens=200
    )
    return response['choices'][0]['message']['content']

# 응답 타입 감지 함수
def detect_response_type(prompt):
    image_keywords = ['그림', '이미지', '사진', '그려줘', '그려', '그림으로', '사진으로', '이미지로']
    if any(keyword in prompt for keyword in image_keywords):
        return 'image'
    else:
        return 'text'
