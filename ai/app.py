from flask import Flask, request, jsonify
from openai_api import generate_text
from datetime import datetime

app = Flask(__name__)

@app.route('/chat', methods=['POST'])
def generate_text_route():
    data = request.get_json()
    prompt = data.get('prompt')
    if not prompt:
        return create_response("No prompt provided", "프롬프트가 정상적으로 전달되지 않았습니다.", 400, {})
    
    try:
        generated_text = generate_text(prompt)
        response_data = {"generated_text": generated_text}
        return create_response("GENERATED_SUCCESSFULLY", f"gpt4-o가 생성한 답변입니다.", 201, response_data)
    except Exception as e:
        return create_response(str(e), "Internal server error.", 500, {})

def create_response(message, details, http_status, data):
    response = {
        "timeStamp": datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        "code": http_status,
        "message": message,
        "details": details,
        "data": data
    }
    return jsonify(response), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)
