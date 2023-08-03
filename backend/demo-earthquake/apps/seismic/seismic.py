from __future__ import unicode_literals

import json
import logging
import sys
from logging.handlers import RotatingFileHandler

from tornado import gen
from tornado.ioloop import IOLoop
from tornado.websocket import websocket_connect

sys.path.append("./lib/")
from migratorydata_client_python import *

# TODO set config variables
################################################################################
########################    CONFIGURATION    ###################################
migratory_data_subject = '/seismic/data'
server = "127.0.0.1:8800"
encryption = 'False'
token = "some-token"

if len(sys.argv) == 5:
    server = sys.argv[1]
    migratory_data_subject = sys.argv[2]
    encryption = sys.argv[3]
    token = sys.argv[4]
################################################################################

class MyListener(MigratoryDataListener):
    def __init__(self):
        pass

    def on_status(self, status, info):
        logging.info("Got status " + status + " - " + info)

    def on_message(self, message):
        logging.info("Got message " + str(message))

class MyLogListener(MigratoryDataLogListener):
    def __init__(self):
        pass

    def on_log(self, message, migratoryDataLogLevel):
        if migratoryDataLogLevel == MigratoryDataLogLevel.TRACE:
            logging.debug(message)
        if migratoryDataLogLevel == MigratoryDataLogLevel.DEBUG:
            logging.debug(message)
        if migratoryDataLogLevel == MigratoryDataLogLevel.INFO:
            logging.info(message)
        if migratoryDataLogLevel == MigratoryDataLogLevel.WARN:
            logging.warning(message)
        if migratoryDataLogLevel == MigratoryDataLogLevel.ERROR:
            logging.error(message)

echo_uri = 'wss://www.seismicportal.eu/standing_order/websocket'
PING_INTERVAL = 15


def myprocessing(message, migratoryDataClient):
    try:
        data = json.loads(message)

        content = {}
        content['Datetime(UTC)'] = data['data']['properties']['time']
        content['Region'] = data['data']['properties']['flynn_region']
        content['lat*'] = data['data']['properties']['lat']
        content['long*'] = data['data']['properties']['lon']
        content['Depth(Km)'] = data['data']['properties']['depth']
        content['&Mag'] = data['data']['properties']['mag']
        contentJson = json.dumps(content)
        closure = data['data']['properties']['flynn_region']
        logging.info("Message conent: " + contentJson)
        migratoryDataClient.publish(MigratoryDataMessage(migratory_data_subject, contentJson.encode('utf-8'), closure))

    except Exception:
        logging.exception("Unable to parse json message")


@gen.coroutine
def listen(ws, migratoryDataClient):
    while True:
        msg = yield ws.read_message()
        if msg is None:
            logging.info("close")
            self.ws = None
            break
        myprocessing(msg, migratoryDataClient)


@gen.coroutine
def launch_client(migratoryDataClient):
    try:
        logging.info("Open WebSocket connection to %s", echo_uri)
        ws = yield websocket_connect(echo_uri, ping_interval=PING_INTERVAL)
    except Exception:
        logging.exception("connection error")
    else:
        logging.info("Waiting for messages...")
        listen(ws, migratoryDataClient)


def start_migratory_data_client():
    client = MigratoryDataClient()

    client.set_log_listener(MyLogListener(), MigratoryDataLogLevel.INFO)

    client.set_entitlement_token(token)

    client.set_listener(MyListener())

    client.set_servers([server])

    client.set_encryption(encryption == 'True')

    client.connect()

    return client


if __name__ == '__main__':
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(levelname)s - %(message)s',
        handlers=[
            logging.StreamHandler(),
            RotatingFileHandler('seismic.log', maxBytes=100000000, backupCount=7)
        ]
    )

    migratoryDataClient = start_migratory_data_client()
    ioloop = IOLoop.instance()
    launch_client(migratoryDataClient)
    try:
        ioloop.start()
    except KeyboardInterrupt:
        logging.info("Close WebSocket")
        migratoryDataClient.disconnect()
        ioloop.stop()
