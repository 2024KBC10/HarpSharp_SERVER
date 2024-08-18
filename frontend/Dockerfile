FROM node:18-alpine

RUN mkdir -p /app
WORKDIR /app

# Install dependencies based on the preferred package manager
COPY . .

RUN npm install

EXPOSE 3000

CMD ["npm", "run", "dev"]
