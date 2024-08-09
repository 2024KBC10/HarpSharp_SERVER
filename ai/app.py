from flask import Flask, request, jsonify
from openai_api import generate_text

app = Flask(__name__)

@app.route('/chat', methods=['POST'])
def generate_text_route():
    data = request.get_json()
    prompt = data.get('prompt')
    if not prompt:
        return jsonify({'error': 'No prompt provided'}), 400
    
    try:
        generated_text = generate_text(prompt)
        return jsonify({'generated_text': generated_text})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)
