FROM node:16.10-alpine AS build
COPY . .
RUN npm install

CMD ["sh", "-c", "npm run build"]
