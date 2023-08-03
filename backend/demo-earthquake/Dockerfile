FROM python:3.9.17-slim-bullseye
RUN pip3 install tornado
COPY . /

ENV SERVER 127.0.0.1:8800
ENV SUBJECT /migratorydata/seismic/info
ENV ENCRYPTION False
ENV TOKEN some-token

ENTRYPOINT exec python apps/seismic/seismic.py $SERVER $SUBJECT $ENCRYPTION $TOKEN