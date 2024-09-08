# history_service.py

from pymongo import MongoClient
from datetime import datetime

class HistoryService:
    def __init__(self, db_url):
        self.client = MongoClient(db_url)
        self.db = self.client.chatbot
        self.collection = self.db.history

    def save_history(self, username, question, answer):
        self.collection.insert_one({
            'username': username,
            'question': question,
            'answer': answer,
            'timestamp': datetime.now()
        })

    def get_history(self, username):
        return list(self.collection.find({'username': username}).sort("timestamp", -1))

    def clear_history(self, username):
        self.collection.delete_many({'username': username})
