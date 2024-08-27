import unittest
from unittest.mock import patch
from openai_api import generate_image_from_text

class TestOpenAIApi(unittest.TestCase):
    
    @patch('openai.Image.create')
    def test_generate_image_from_text(self, mock_create):
        # 모의 응답 설정
        mock_create.return_value = {
            'data': [{'url': 'https://example.com/fake-image-url.png'}]
        }
        
        prompt = "바다에서 수영하는 상어를 그려줘."
        result = generate_image_from_text(prompt)
        
        # 테스트에서 기대하는 URL과 실제 결과를 비교
        self.assertEqual(result, 'https://example.com/fake-image-url.png')

if __name__ == '__main__':
    unittest.main()
