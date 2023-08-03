#!/usr/bin/env sh

docker build -t demo-others ./backend/demo-others/

docker build -t demo-earthquake ./backend/demo-earthquake/

docker build -t demo-frontend ./frontend/

docker compose up