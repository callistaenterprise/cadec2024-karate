FROM node:21-alpine AS external-website

WORKDIR /app

COPY . .

# Clean install all node modules
RUN npm ci

RUN npm run build

# Delete source code files that were used to build the app that are no longer needed
RUN rm -rf src/ static/

RUN chown -R node:node /app

USER node:node

CMD ["node","build/index.js"]