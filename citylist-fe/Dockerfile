FROM node:16.10-alpine AS build
WORKDIR /usr/src/app
COPY . .
RUN npm install

CMD ["sh", "-c", "npm run build"]

FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/aston-villa-app /usr/share/nginx/html
