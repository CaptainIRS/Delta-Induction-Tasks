FROM python:3.8-slim

COPY /app /app
WORKDIR /app

RUN pip3 install -r /app/requirements.txt

EXPOSE 5000
CMD [ "python3", "-u", "app.py" ]
