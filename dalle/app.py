
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import JSONResponse, StreamingResponse
from pydantic import BaseModel
import asyncio
from openai_api import detect_response_type, generate_text, generate_image_from_text
from io import BytesIO
from datetime import datetime

import time 
from loguru import logger

app = FastAPI()

logger.add("file_{time}.log", rotation="1 day")

# 응답 시간 체크 
'''
@app.middleware("http")
async def log_process_time(request: Request, call_next):
    start_time = time.time() 
    response = await call_next(request)
    process_time = time.time() - start_time  
    logger.info(f"Request: {request.method} {request.url} - Process time: {process_time:.4f} seconds")
    return response
'''

class PromptRequest(BaseModel):
    prompt: str

@app.post("/chat")
async def generate_route(data: PromptRequest):
    prompt = data.prompt

    if not prompt:
        raise HTTPException(status_code=400, detail="잘못된 입력 값 입니다.")

    response_type = detect_response_type(prompt)

    try:
        if response_type == 'image':
            image_task = asyncio.create_task(generate_image_from_text(prompt))
            #generated_text = await generate_text(prompt)

            image = await image_task
            if image:
                img_io = BytesIO()
                image.save(img_io, 'PNG')
                img_io.seek(0)
                return StreamingResponse(img_io, media_type="image/png")
            else:
                return create_response("GENERATED_FAILED", "이미지 생성에 실패했습니다.", 500, None)
        else:
            generated_text = await generate_text(prompt)
            response_data = {"generated_text": generated_text}
            return create_response("GENERATED_SUCCESSFULLY", f"gpt4-o가 생성한 답변입니다.", 201, response_data)
    except Exception as e:
            print(f"An error occurred: {e}")
            return create_response("GENERATED_FAILED", str(e), 500, None)

def create_response(message, details, http_status, data):
    response = {
        "timeStamp": datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        "code": http_status,
        "message": message,
        "details": details,
        "data": data
    }
    return JSONResponse(content=response, status_code=200)


if __name__ == '__main__':
    import uvicorn
    uvicorn.run("app:app", host='0.0.0.0', port=5001)
