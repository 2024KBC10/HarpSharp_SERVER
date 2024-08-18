from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv()

key = os.getenv('OPENAI_API_KEY')
if not key:
    raise ValueError("OPENAI_API_KEY is not set in the environment variables.")

client = OpenAI(api_key=key)

def generate_text(prompt, model_name='gpt-4o-mini'):
    response = client.chat.completions.create(
        model=model_name,
        messages=[
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": prompt}
        ],
        max_tokens=200
    )
    return response.choices[0].message.content
