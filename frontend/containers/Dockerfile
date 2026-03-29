# Stage 1: build Vue app
FROM node:20-alpine AS build

WORKDIR /app

# Copy package.json & install dependencies
COPY ../../package*.json ./
RUN npm install

# Copy Vue source
COPY ../../ ./

# Build production
RUN npm run build

# Stage 2: serve built files with "serve"
FROM node:20-alpine

WORKDIR /app

# Install static server
RUN npm install -g serve

# Copy built files
COPY --from=build /app/dist ./dist

# Expose port for API gateway
EXPOSE 3100

# Start SPA
CMD ["serve", "-s", "dist", "-l", "3100"]