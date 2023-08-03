<script setup>

import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';

import { onMounted } from 'vue';
import AndroidCode from './apis/android-code.vue';
import CppCode from './apis/cpp-code.vue';
import CsharpCode from './apis/csharp-code.vue';
import IosCode from './apis/ios-code.vue';
import JavaCode from './apis/java-code.vue';
import JavascriptCode from './apis/javascript-code.vue';
import PhpCode from './apis/php-code.vue';
import PythonCode from './apis/python-code.vue';
import ReactphpCode from './apis/reactphp-code.vue';
import RestCode from './apis/rest-code.vue';

import { ref } from 'vue'

const props = defineProps(["endpoints", "tab", "info", "title"]);

const server = "{{ SERVER_ADDRESS }}"
const token = "{{ API_KEY }}"

const popup = ref(null)
const popupContent = ref(null)
const tableJson = ref(null)

let selectedEndpoint = ref(null)
let selectedCode = ref('')
let receivedMessage = ref({})
let activeColor = ref('red')
let lastUpdateInfo = ref('')

let client = null;
let lastUpdateTime = Date.now()

let dynamicMap = null;
let dynamicTable = null;

function selectApi (api) {
    selectedCode.value = api;
}
function selectEndpoint(endpoint) {
    selectedEndpoint.value = endpoint;
    if (endpoint) {
        resubscribe(endpoint);
    }

}
function resubscribe(endpoint) {
    var subjects = client.getSubjects();
    if (subjects.length > 0) {
        client.unsubscribe(client.getSubjects());
    }

    if (props.tab == 'map') {
        if (dynamicMap) {
            dynamicMap.resize = true;
        } else {
            dynamicMap = new DynamicMap(popup.value, popupContent.value);
        }
    } else {
        if (dynamicTable) {
            dynamicTable.remove();
            dynamicTable = null;
        }
        dynamicTable = new DynamicTable(tableJson.value);
    }

    if (endpoint.endpointExample) {
        client.subscribe([endpoint.endpointExample]);
    } else {
        client.subscribe([endpoint.endpoint]);
    }
}

function getLastSegment(endpoint) {
    return endpoint.substr(endpoint.indexOf("/", endpoint.indexOf("/", 1) + 1) + 1);
}

onMounted(() => {

    client = new MigratoryDataClient();

    client.setStatusHandler((status) => {
        console.log(status.type + ":" + status.info);
        if (MigratoryDataClient.NOTIFY_SERVER_UP == status.type) {
            activeColor.value = 'green';
        } else if (MigratoryDataClient.NOTIFY_SERVER_DOWN == status.type) {
            activeColor.value = 'red';
        }
        console.log(activeColor.value);
    });

    client.setMessageHandler((message) => {
        //console.log(message.subject + ":" + message.content);
        
        try {
            receivedMessage.value = JSON.parse(message.content);
        } catch (e) {
            receivedMessage.value = {error: "Invalid json data."};
        }

        lastUpdateTime = Date.now();
        lastUpdateInfo.value = timeDifference(Date.now(), lastUpdateTime);

        if (dynamicMap) {
            dynamicMap.updateMap(message.content);
        } else {
            dynamicTable.createOrUpdateTable(message.content);
        }
    });

    client.setEntitlementToken("some-token");
    client.setServers(["http://127.0.0.1:8800"]);

    client.connect();

    selectEndpoint(props.endpoints[0]);

    setInterval(() => {
        lastUpdateInfo.value = timeDifference(Date.now(), lastUpdateTime);
    }, 5000);

});

</script>




<template>
        <div class="container">

        <!-- side vertical menu -->
        <div class="row">
        <div class="col-2">

            <div class="btn-group-vertical">
                <input @click="selectEndpoint(null)" type="radio" class="btn-check" name="options" id="overview" autocomplete="off" checked>
                <label class="btn btn-light text-start mb-1 mt-3" for="overview" style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
                    API Overview
                </label>

                <template v-for="(endpoint, index) in endpoints">
                    <template v-if="endpoint.endpointExample">
                                    <input type="radio" class="btn-check" name="options" autocomplete="off">
                                    <label class="btn btn-light text-start mb-1" :for="index"
                                        style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
                                        <div v-if="endpoint.operation === 'sub'">
                                            <code class="text-primary">SUB</code> {{ getLastSegment(endpoint.endpoint) }}
                                        </div>
                                        <div v-else-if="endpoint.operation === 'pub'">
                                            <code class="text-success">PUB</code> {{ getLastSegment(endpoint.endpoint) }}
                                        </div>
                                        <div v-else>
                                            <code class="text-success">PUB</code> <code class="text-primary">SUB</code> {{
                                                getLastSegment(endpoint.endpoint) }}
                                        </div>
                                    </label>
                                    <input @click="selectEndpoint(endpoint)" type="radio" class="btn-check" name="options"
                                            :id="index" autocomplete="off" :checked="index == 0">
                                        <label class="btn btn-light text-start mb-1" :for="index"
                                            style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
                                            <div v-if="endpoint.operation === 'sub'">
                                                <i class="bi bi-forward"></i>
                                                <code class="text-primary">SUB</code> {{ getLastSegment(endpoint.endpointExample) }}
                                            </div>
                                            <div v-else-if="endpoint.operation === 'pub'">
                                                <i class="bi bi-forward"></i>
                                                <code class="text-success">PUB</code> {{ getLastSegment(endpoint.endpointExample) }}
                                            </div>
                                            <div v-else>
                                                <i class="bi bi-forward"></i>
                                                <code class="text-success">PUB</code> <code class="text-primary">SUB</code> {{
                                                    getLastSegment(endpoint.endpointExample) }}
                                            </div>
                                        </label>
                                </template>
                                <template v-else>
                                    <input @click="selectEndpoint(endpoint)" type="radio" class="btn-check" name="options"
                                        :id="index" autocomplete="off" :checked="index == 0">
                                    <label class="btn btn-light text-start mb-1" :for="index"
                                        style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
                                        <div v-if="endpoint.operation === 'sub'">
                                            <code class="text-primary">SUB</code> {{ getLastSegment(endpoint.endpoint) }}
                                        </div>
                                        <div v-else-if="endpoint.operation === 'pub'">
                                            <code class="text-success">PUB</code> {{ getLastSegment(endpoint.endpoint) }}
                                        </div>
                                        <div v-else>
                                            <code class="text-success">PUB</code> <code class="text-primary">SUB</code> {{
                                                getLastSegment(endpoint.endpoint) }}
                                        </div>
                                    </label>
                                </template>
                </template>
            </div>

            
        </div>



        <div class="col-8 mt-3">
            <h3 class="card-subtitle mb-2 text-muted">{{ title }}</h3>
            <div class="mb-4"><span class="mute">{{ info }}</span></div>

            <div class="row mt-3">
                <div class="mb-4">
                    <span class="pe-3">
                        <i id="connectStatus" class="fs-6 bi bi-circle-fill" :style="{ color: activeColor }"></i> Last update: {{ lastUpdateInfo }}
                    </span>
                </div>
            </div>
                    <!-- example demo -->
                    <div class="row mt-3">
                        <div class="tabset">
                            <template v-if="tab == 'map'">
                                <input type="radio" name="tabset" id="tab-map" aria-controls="map" checked>
                                <label for="tab-map">Map</label>
                            </template>
                            <template v-if="tab == 'table'">
                                <input type="radio" name="tabset" id="tab-table" aria-controls="table" checked>
                                <label for="tab-table">Table</label>
                            </template>
                            <input type="radio" name="tabset" id="tab-data" aria-controls="data">
                            <label for="tab-data">Data</label>
                            <input type="radio" name="tabset" id="tab-code" aria-controls="code">
                            <label for="tab-code">Code</label>
                            <div class="tab-panels">
                                <template v-if="tab == 'map'">
                                    <section id="map-tab" class="tab-panel">
                                        <div id="map-view">
                                            <div id="map" class="map"></div>
                                            <div ref="popup" class="ol-popup">
                                                <div ref="popupContent"></div>
                                            </div>
                                        </div>
                                    </section>
                                </template>
                                <template v-if="tab == 'table'">
                                    <section class="tab-panel">
                                        <div id="table" role="tabpanel">
                                            <div class="table-responsive" ref="tableJson"></div>
                                        </div>
                                    </section>
                                </template>
                                <section id="data" class="tab-panel">
                                    <vue-json-pretty :data="receivedMessage" :show-icon="true" :show-length="true" :deep="1"
                                        :show-line-number="true" />
                                </section>
                                <section id="code" class="tab-panel">
                                    <div v-if="selectedEndpoint != null" class="stepper d-flex flex-column">

                                        <h5>Test endpoint <template v-for="endpoint in endpoints"><label
                                                    v-show="selectedEndpoint.endpoint == endpoint.endpoint"><code>{{ endpoint.endpoint }}</code></label></template>
                                        </h5>
                                        <div class="mb-4"><span class="mute">Below you can find the code for various
                                                languages to test this
                                                endpoint.</span></div>

                                        <div class="alert alert-warning alert-dismissible fade show col-md-8 mb-4"
                                            role="alert">
                                            <strong>An API key is required to run the code below!</strong> Create an API key
                                            by logging in,
                                            adoping
                                            this API by one of your APIs, and generate a key to include this endpoint.
                                            Finally, replace in
                                            the code
                                            below <code>{{ token }}</code> with key you generated.
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close" v-on:click="app_creation_failed = false"></button>
                                        </div>

                                        <div class="d-flex mb-1">
                                            <div class="d-flex flex-column pr-4 align-items-center">
                                                <i class="bi bi-1-circle fs-3" style="color: #48B4E3;"></i>
                                                <div class="line h-100"></div>
                                            </div>
                                            <div>
                                                <h5 class="text-dark mx-2 mt-2 mb-5">Choose your language</h5>

                                                <ul class="nav nav-pills my-5" id="myTab" role="tablist">
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('java')" class="nav-link"
                                                            id="java-tab" data-bs-toggle="tab"
                                                            data-bs-target="#java-tab-pane" type="button" role="tab"
                                                            aria-controls="java-tab-pane" aria-selected="true">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNNDcuNjE3IDk4LjEycy00Ljc2NyAyLjc3NCAzLjM5NyAzLjcxYzkuODkyIDEuMTMgMTQuOTQ3Ljk2OCAyNS44NDUtMS4wOTIgMCAwIDIuODcxIDEuNzk1IDYuODczIDMuMzUxLTI0LjQzOSAxMC40Ny01NS4zMDgtLjYwNy0zNi4xMTUtNS45Njl6TTQ0LjYzIDg0LjQ1NXMtNS4zNDggMy45NTkgMi44MjMgNC44MDVjMTAuNTY3IDEuMDkxIDE4LjkxIDEuMTggMzMuMzU0LTEuNiAwIDAgMS45OTMgMi4wMjUgNS4xMzIgMy4xMzEtMjkuNTQyIDguNjQtNjIuNDQ2LjY4LTQxLjMwOS02LjMzNnoiIGZpbGw9IiMwMDc0QkQiLz48cGF0aCBkPSJNNjkuODAyIDYxLjI3MWM2LjAyNSA2LjkzNS0xLjU4IDEzLjE3LTEuNTggMTMuMTdzMTUuMjg5LTcuODkxIDguMjY5LTE3Ljc3N2MtNi41NTktOS4yMTUtMTEuNTg3LTEzLjc5MiAxNS42MzUtMjkuNTggMCAuMDAxLTQyLjczMSAxMC42Ny0yMi4zMjQgMzQuMTg3eiIgZmlsbD0iI0VBMkQyRSIvPjxwYXRoIGQ9Ik0xMDIuMTIzIDEwOC4yMjlzMy41MjkgMi45MS0zLjg4OCA1LjE1OWMtMTQuMTAyIDQuMjcyLTU4LjcwNiA1LjU2LTcxLjA5NC4xNzEtNC40NTEtMS45MzggMy44OTktNC42MjUgNi41MjYtNS4xOTIgMi43MzktLjU5MyA0LjMwMy0uNDg1IDQuMzAzLS40ODUtNC45NTMtMy40ODctMzIuMDEzIDYuODUtMTMuNzQzIDkuODE1IDQ5LjgyMSA4LjA3NiA5MC44MTctMy42MzcgNzcuODk2LTkuNDY4ek00OS45MTIgNzAuMjk0cy0yMi42ODYgNS4zODktOC4wMzMgNy4zNDhjNi4xODguODI4IDE4LjUxOC42MzggMzAuMDExLS4zMjYgOS4zOS0uNzg5IDE4LjgxMy0yLjQ3NCAxOC44MTMtMi40NzRzLTMuMzA4IDEuNDE5LTUuNzA0IDMuMDUzYy0yMy4wNDIgNi4wNjEtNjcuNTQ0IDMuMjM4LTU0LjczMS0yLjk1OCAxMC44MzItNS4yMzkgMTkuNjQ0LTQuNjQzIDE5LjY0NC00LjY0M3ptNDAuNjk3IDIyLjc0N2MyMy40MjEtMTIuMTY3IDEyLjU5MS0yMy44NiA1LjAzMi0yMi4yODUtMS44NDguMzg1LTIuNjc3LjcyLTIuNjc3Ljcycy42ODgtMS4wNzkgMi0xLjU0M2MxNC45NTMtNS4yNTUgMjYuNDUxIDE1LjUwMy00LjgyMyAyMy43MjUgMC0uMDAyLjM1OS0uMzI3LjQ2OC0uNjE3eiIgZmlsbD0iIzAwNzRCRCIvPjxwYXRoIGQ9Ik03Ni40OTEgMS41ODdTODkuNDU5IDE0LjU2MyA2NC4xODggMzQuNTFjLTIwLjI2NiAxNi4wMDYtNC42MjEgMjUuMTMtLjAwNyAzNS41NTktMTEuODMxLTEwLjY3My0yMC41MDktMjAuMDctMTQuNjg4LTI4LjgxNUM1OC4wNDEgMjguNDIgODEuNzIyIDIyLjE5NSA3Ni40OTEgMS41ODd6IiBmaWxsPSIjRUEyRDJFIi8+PHBhdGggZD0iTTUyLjIxNCAxMjYuMDIxYzIyLjQ3NiAxLjQzNyA1Ny0uOCA1Ny44MTctMTEuNDM2IDAgMC0xLjU3MSA0LjAzMi0xOC41NzcgNy4yMzEtMTkuMTg2IDMuNjEyLTQyLjg1NCAzLjE5MS01Ni44ODcuODc0IDAgLjAwMSAyLjg3NSAyLjM4MSAxNy42NDcgMy4zMzF6IiBmaWxsPSIjMDA3NEJEIi8+PC9zdmc+">
                                                            <br>Java
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('nodejs')" class="nav-link"
                                                            id="nodejs-tab" data-bs-toggle="tab"
                                                            data-bs-target="#nodejs-tab-pane" type="button" role="tab"
                                                            aria-controls="nodejs-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTEyLjc3MSAzMC4zMzRMNjguNjc0IDQuNzI5Yy0yLjc4MS0xLjU4NC02LjQwMi0xLjU4NC05LjIwNSAwTDE0LjkwMSAzMC4zMzRDMTIuMDMxIDMxLjk4NSAxMCAzNS4wODggMTAgMzguNDA3djUxLjE0MmMwIDMuMzE5IDIuMDg0IDYuNDIzIDQuOTU0IDguMDgzbDExLjc3NSA2LjY4OGM1LjYyOCAyLjc3MiA3LjYxNyAyLjc3MiAxMC4xNzggMi43NzIgOC4zMzMgMCAxMy4wOTMtNS4wMzkgMTMuMDkzLTEzLjgyOHYtNTAuNDljMC0uNzEzLS4zNzEtMS43NzQtMS4wNzEtMS43NzRoLTUuNjIzQzQyLjU5NCA0MSA0MSA0Mi4wNjEgNDEgNDIuNzczdjUwLjQ5YzAgMy44OTYtMy41MjQgNy43NzMtMTAuMTEgNC40OEwxOC43MjMgOTAuNzNjLS40MjQtLjIzLS43MjMtLjY5My0uNzIzLTEuMTgxVjM4LjQwN2MwLS40ODIuNTU1LS45NjYuOTgyLTEuMjEzbDQ0LjQyNC0yNS41NjFjLjQxNS0uMjM1IDEuMDI1LS4yMzUgMS40MzkgMGw0My44ODIgMjUuNTU1Yy40Mi4yNTMuMjcyLjcyMi4yNzIgMS4yMTl2NTEuMTQyYzAgLjQ4OC4xODMuOTYzLS4yMzIgMS4xOThsLTQ0LjA4NiAyNS41NzZjLS4zNzguMjI3LS44NDcuMjI3LTEuMjYxIDBsLTExLjMwNy02Ljc0OWMtLjM0MS0uMTk4LS43NDYtLjI2OS0xLjA3My0uMDg2LTMuMTQ2IDEuNzgzLTMuNzI2IDIuMDItNi42NzcgMy4wNDMtLjcyNi4yNTMtMS43OTcuNjkyLjQxIDEuOTI5bDE0Ljc5OCA4Ljc1NGE5LjI5NCA5LjI5NCAwIDAwNC42NDcgMS4yNDZjMS42NDIgMCAzLjI1LS40MjYgNC42NjctMS4yNDZsNDMuODg1LTI1LjU4MmMyLjg3LTEuNjcyIDQuMjMtNC43NjQgNC4yMy04LjA4M1YzOC40MDdjMC0zLjMxOS0xLjM2LTYuNDE0LTQuMjI5LTguMDczek03Ny45MSA4MS40NDVjLTExLjcyNiAwLTE0LjMwOS0zLjIzNS0xNS4xNy05LjA2Ni0uMS0uNjI4LS42MzMtMS4zNzktMS4yNzItMS4zNzloLTUuNzMxYy0uNzA5IDAtMS4yNzkuODYtMS4yNzkgMS41NjYgMCA3LjQ2NiA0LjA1OSAxNi41MTIgMjMuNDUzIDE2LjUxMiAxNC4wMzkgMCAyMi4wODgtNS40NTUgMjIuMDg4LTE1LjEwOSAwLTkuNTcyLTYuNDY3LTEyLjA4NC0yMC4wODItMTMuODg2LTEzLjc2Mi0xLjgxOS0xNS4xNi0yLjczOC0xNS4xNi01Ljk2MiAwLTIuNjU4IDEuMTg0LTYuMjAzIDExLjM3NC02LjIwMyA5LjEwNSAwIDEyLjQ2MSAxLjk1NCAxMy44NDIgOC4wOTEuMTE4LjU3Ny42NDUuOTkxIDEuMjQuOTkxaDUuNzU0Yy4zNTQgMCAuNjkyLS4xNDMuOTQtLjM5Ni4yNC0uMjcyLjM2Ny0uNjEzLjMzNS0uOTc5LS44OTEtMTAuNTY4LTcuOTEyLTE1LjQ5My0yMi4xMTItMTUuNDkzLTEyLjYzMSAwLTIwLjE2NiA1LjMzNC0yMC4xNjYgMTQuMjc1IDAgOS42OTggNy40OTcgMTIuMzc4IDE5LjYyMiAxMy41NzcgMTQuNTA1IDEuNDIyIDE1LjYzMyAzLjU0MiAxNS42MzMgNi4zOTUgMCA0Ljk1NS0zLjk3OCA3LjA2Ni0xMy4zMDkgNy4wNjZ6IiBmaWxsPSIjODNDRDI5Ii8+PC9zdmc+">
                                                            <br>Node.js
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('android')" class="nav-link"
                                                            id="android-tab" data-bs-toggle="tab"
                                                            data-bs-target="#android-tab-pane" type="button" role="tab"
                                                            aria-controls="android-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAABmJLR0QA/wD/AP+gvaeTAAAEtElEQVR4nO3cz2scZRgH8O87s5ukDemPSFVIQEHxRwVFeyhSCpsszVLaNaj1JHjwUBARxQbM0mwdMWnxB/Wg4KX4B0gxlV2DMWn0YhHUgr1ItT30ULYICVoJutnZeTzsBte42d2Zd+Z9Ztvnc2rmx/PMO9/s7jvJ2wBCCCGEEEIIIYQQt6eJ+bH+3Nz+XVHVdz4bu3Nifqw/qvpBWNwXsO54cfS5vjW3ZFeTN6YKo07Y9fPFkbfcpFvqW3NLU4X0kbDrBxWbAEA4CWAAtWs6kS+m94dV+kRxJEOk8vXaAwo0E1ZtXbEJQAErjV8S0cdHf9iT1K3rzKZ2eKTO1FrUENRKi1OMik0AsNSbG7Y8squ0/ZhuWde2PgQw3LCJLOWF/hYXlGp/iDn5QvosgZ5t2PRXwks+WrEqgwrqSQBPQNF9INwDoB/AjvpxvwNYhcI1kLoK4KIiXCCFIYBmG3sQ8NFMdukVIwPqQIL7Ahp5oFcVcADAtvqmLRVr7ZKC2gJQbQs1PXUngJ0gDAO0D8ALpOolGxBwpdyTmIzm6oOJz1sQgJns0nUifNK4rXbzA/t3fApkEb33fuarVY16oYvNW5Azd3Cb6/19GqReRHTXRQDOrHnlY++Of/tnRD18iUUAuXPp3QmbZgl4wFDLy1DW09OHF3821G9T7AHkvxjZS56aAzBouPWyInXw7afOf2+473+wBuB8nrrftawLACL78UMby/Bo3/T415eZ+vN9CE/Mj/W7llXE5jf/R4AOeYoOA7gYoEUn598BS806hezWAPVDwTYN7Su7p6Dw4KYHePT8+ndmrpC6asPy937d+fkPu1g9CeA1X/VDwvIKyJ1L74bCS62OqWy1b6z/uzfZ+5vfHj7PfzlXSD3kt0cYWAKwbZpEm1dfT7n6OgjKcRyrWnE3/piiLZ/nJyxlveG3RxiMfwg7X2YG3UqlBKCng8OvA7AB3B2wnZ/zy5U+6653Diz+EbBXIMZfARXXzaKzmw8AQwh+8/2e39tTpqxGr0CMB6DIGzHds1MEL2W6J8NngHrcfM8OkbXHdEuOD+F7GXp2iGJ8bUIIIW41oTyIOXOpYbdqfQAgAwAgLEDZk9PZhV83HjtVGG3+S8WYmM4u/e+e+BmfX9qzoPrF/QTgCGrregag8AxQ/e54YXRItz63qMenHUD9O6PZL1MGlcJp3frcoh5fGM8BmU33UIt93SPS8YURwECLfdtDqM8t0vHFalnK7UgCYCYBMJMAmEkAzCQAZhIAMwmAmQTATAJgJgEwkwCYSQDMJABmEgAzCYCZBMAsjAButthndKl3RCIdn34AhMUWexe063OLeHzaAVRtlQOw3GTXSsLzcrr1uUU9Pu0ATh06/wsBj0HhU9RerjcBnE143l5n/JsruvW5RT0+4/9FqRtXxkVJZkHMJABmEgAzjgBazau5GX9uMR9A63k1N+PPLcYDaDGv5sby3GI8gCbzam6szy3sf7Bpo3bPCe3m6brnmyazIGYSADMJgJkEwEwCYCYBMJMAmEkAzCQAZnEMQHcVQlet0ohfALqrELpslUbsAtBdhdBtqzRiF4DuKoRbfZWGEEIIIYQQQgghhOhe/wD/AJRkygRcYwAAAABJRU5ErkJggg==">
                                                            <br>Android
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('ios')" class="nav-link" id="ios-tab"
                                                            data-bs-toggle="tab" data-bs-target="#ios-tab-pane"
                                                            type="button" role="tab" aria-controls="ios-tab-pane"
                                                            aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAYAAADimHc4AAAABmJLR0QA/wD/AP+gvaeTAAAFMUlEQVR4nO2dW2hdRRSGv8S0TVvT9CZ4wYQo2FatF8QLFixildpStRT0xbsoIuiLqK9VQfoiioj6og+1ghdE8BKopYaK19p6q31QrDVQxKqJsa2amDbHh5UDSZrk7DN7zV5zZs8H6zEzK/+aPXv2mllzIJFIJBKJRCKRSJSNJmsHDFgO3ABcBXQBnbbulIMm4HrgU6AyxoYtnSoLy4GPGS981Q4b+hU9TcBDwH9MLn4F+NnMu8iZBbzO1MJXbZuVg1VarB3wQCvwLvKSrcXXnn0pHc3Am9Qe+VVbY+NmvDxAdvH7gdk2bsZJF7KqyRqAp2zcjJcesos/Aiy1cTNOVpFd/Arwto2b8fIe2cX/FzjTxs04OQVJKWQNwGM2bsbLnWQX/xPSykedl8km/rfAIiMfo+ZLaou/kyS+N/qZXvwtpGnHK4NM/aV7i6FfpeEI44U/AjxJg0w5MWRD9wNtwG5gK/AqcMjUozooek94BpImvhy4FPkgWgTMA/pG7TvgcyRX/41y/xcgX80XITtlC4EFyJMzAPQCXyE7aN3An8r9m3EO8DzwB/WlDH4EHkUSba50Ih9f++vsexj5wr4WSXM3JMuQnalj1PfPT7RjwAfArcDcDP22ICcfuhX6rgB7gescNTBhFrARGCL/Pz/RDgGbgduQAM9HdsE6gLVImvkXD/1WgO3AGYo6eaEL2erzIUAIdhi4R00tZVYCv2MvUhH2EvLkBcM1SKrXWpgi7UOgXUO8vFwJ/IO9IBa2C5iTX0J3Oql/eRmL7QNW55fQnVZkBFgLYWHPEUCS7wnshSjajgKPaIiXl6X4WeeHbCPA7RriabAFe0GKto0qyinQQX0b4TFYDx6Sl65JppuJI5WdlSHgPiQQQRBzqmEye0ZHNh1OQl5G1qIUZSPAEhXlJsFlClpJuYr7eoDvfTXuEoAL1b0Im/d9Nu4SgLPVvQibHp+NuwQgz/ZgI7LPZ+MuAVis7kW4/I0cFPCGSwAWqnsRLkO+O3AJwAx1L8LF+46XSwDKtASdjeeUs0sAvD+WAdGEnLzwhksA+tW9CJvzfTaeAlCbtT4bdwlAr7oXYbMaj5vuLgH4Sd2LsJkL3OGrcZcAeEtMBczDwEwfDZ/g8DfNwN3ajgROO3Lo7CNrR0A+Tqa7BClWGwTOVdBvHC5T0CDwhbYjDUD1EijV0ifXPWHzm6aMWAa8g/FRRJASI+spwdI+A07OrWIOmoED2Athab3AZRpCujACvJG38wanA7l7YjNGJbEXYz8KQ7E+YBNwWi5FHSjryeiprO7VYd7yyxdy/n1sbCq6wznAQexHXgi2B4cB7ZKKGMvwaBurcrYTA/cjVf6F00Z5S5SqthfH6TzvEwCSFxrCuFbKmLswzhK3IKPAeiRa2FsK+qmwBnsxirY+4HQN8bR4BXtRirT1OrLpsZjyLEuDKtoYyzriL+DYhewP5EZjFTSRH5CN7BUe2g6BAeBqZOkdLC3IRRbWI9WHbVDUySunEt/74GlVhQrgEo6/VrJRrZsGLc1dj9yvYC1gHtsJnKgtTJHcSOMeZdmNx6KUIs/6r0M+1NoU2jqA3IZ+EPgLCe585A7Qs5BCQo1Ckq3ATaN9RMESJG/uMhJ3APciL/datCL3fb6I2zvoKPA4DXxf6HTMBB5EbqWtJcQA8Cz5TqS1I7n6rMnC7cB5OfqrC8tyo3nIL5xuQJ6MTmTk/YrMu9uA15BKRS2uGO1vBZJEWwD8hvym5A7k3uk9iv0lEolEIpFIJBKJxHH8D6XMYBifClnNAAAAAElFTkSuQmCC">
                                                            <br>iOS
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('csharp')" class="nav-link"
                                                            id="csharp-tab" data-bs-toggle="tab"
                                                            data-bs-target="#csharp-tab-pane" type="button" role="tab"
                                                            aria-controls="csharp-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBjbGlwLXBhdGg9InVybCgjY2xpcDApIj48cGF0aCBkPSJNMTE1LjUgMzMuNWwuMy0uMmMtLjYtMS4xLTEuNS0yLjEtMi40LTIuNkw2NS4xIDIuOWMtLjgtLjUtMS45LS43LTMuMS0uNy0xLjIgMC0yLjMuMy0zLjEuN2wtNDggMjcuOWMtMS43IDEtMi45IDMuNS0yLjkgNS40djU1LjdjMCAxLjEuMiAyLjMuOSAzLjRsLS4yLjFjLjUuOCAxLjIgMS41IDEuOSAxLjlsNDguMiAyNy45Yy44LjUgMS45LjcgMy4xLjcgMS4yIDAgMi4zLS4zIDMuMS0uN2w0OC0yNy45YzEuNy0xIDIuOS0zLjUgMi45LTUuNFYzNi4xYy4xLS44IDAtMS43LS40LTIuNnptLTUzLjUgNzBjLTIxLjggMC0zOS41LTE3LjctMzkuNS0zOS41UzQwLjIgMjQuNSA2MiAyNC41YzE0LjcgMCAyNy41IDguMSAzNC4zIDIwbC0xMyA3LjVDNzkuMSA0NC41IDcxLjEgMzkuNSA2MiAzOS41Yy0xMy41IDAtMjQuNSAxMS0yNC41IDI0LjVzMTEgMjQuNSAyNC41IDI0LjVjOS4xIDAgMTcuMS01IDIxLjMtMTIuNGwxMi45IDcuNmMtNi44IDExLjgtMTkuNiAxOS44LTM0LjIgMTkuOHpNMTEzIDYyaC0zLjJsLS45IDRoNC4xdjVoLTVsLTEuMiA2aC00LjlsMS4yLTZoLTMuOGwtMS4yIDZoLTQuOGwxLjItNkg5MnYtNWgzLjVsLjktNEg5MnYtNWg1LjNsMS4yLTZoNC45bC0xLjIgNmgzLjhsMS4yLTZoNC44bC0xLjIgNmgyLjJ2NXptLTEyLjcgNGgzLjhsLjktNGgtMy44bC0uOSA0eiIgZmlsbD0iIzY4MjE3QSIvPjwvZz48ZGVmcz48Y2xpcFBhdGggaWQ9ImNsaXAwIj48cGF0aCBmaWxsPSIjZmZmIiBkPSJNMCAwaDEyOHYxMjhIMHoiLz48L2NsaXBQYXRoPjwvZGVmcz48L3N2Zz4=">
                                                            <br>C#
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('cpp')" class="nav-link" id="cpp-tab"
                                                            data-bs-toggle="tab" data-bs-target="#cpp-tab-pane"
                                                            type="button" role="tab" aria-controls="cpp-tab-pane"
                                                            aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTE3LjUgMzMuNWwuMy0uMmMtLjYtMS4xLTEuNS0yLjEtMi40LTIuNkw2Ny4xIDIuOWMtLjgtLjUtMS45LS43LTMuMS0uNy0xLjIgMC0yLjMuMy0zLjEuN2wtNDggMjcuOWMtMS43IDEtMi45IDMuNS0yLjkgNS40djU1LjdjMCAxLjEuMiAyLjMuOSAzLjRsLS4yLjFjLjUuOCAxLjIgMS41IDEuOSAxLjlsNDguMiAyNy45Yy44LjUgMS45LjcgMy4xLjcgMS4yIDAgMi4zLS4zIDMuMS0uN2w0OC0yNy45YzEuNy0xIDIuOS0zLjUgMi45LTUuNFYzNi4xYy4xLS44IDAtMS43LS40LTIuNnpNODIgNjZ2LTRoNXYtNWg1djVoNXY0aC01djVoLTV2LTVoLTV6bTMuMy0xNEM4MS4xIDQ0LjUgNzMuMSAzOS41IDY0IDM5LjVjLTEzLjUgMC0yNC41IDExLTI0LjUgMjQuNXMxMSAyNC41IDI0LjUgMjQuNWM5LjEgMCAxNy4xLTUgMjEuMy0xMi40bDEyLjkgNy42Yy02LjggMTEuOC0xOS42IDE5LjgtMzQuMiAxOS44LTIxLjggMC0zOS41LTE3LjctMzkuNS0zOS41UzQyLjIgMjQuNSA2NCAyNC41YzE0LjcgMCAyNy41IDguMSAzNC4zIDIwbC0xMyA3LjV6TTExNSA2NmgtNXY1aC00di01aC02di00aDZ2LTVoNHY1aDV2NHoiIGZpbGw9IiM5QzAzM0EiLz48L3N2Zz4=">
                                                            <br>C++
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('python')" class="nav-link"
                                                            id="python-tab" data-bs-toggle="tab"
                                                            data-bs-target="#python-tab-pane" type="button" role="tab"
                                                            aria-controls="python-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNNjMuNzI0IDZjLTQuNzM0LjAyNC05LjI1NC40MzItMTMuMjMzIDEuMTQ2LTExLjcyIDIuMDk4LTEzLjg0NiA2LjQ5MS0xMy44NDYgMTQuNTkzdjEwLjY5OWgyNy42OTN2My41NjZIMjYuMjVjLTguMDQ4IDAtMTUuMDk1IDQuOTAzLTE3LjMgMTQuMjM0LTIuNTQ0IDEwLjY5Mi0yLjY1NiAxNy4zNjUgMCAyOC41M0MxMC45MTggODcuMDggMTUuNjIgOTMgMjMuNjY5IDkzaDkuNTIyVjgwLjE3NGMwLTkuMjY2IDcuOTA4LTE3LjQzOSAxNy4zLTE3LjQzOWgyNy42NjNjNy43IDAgMTMuODQ2LTYuNDI3IDEzLjg0Ni0xNC4yNjRWMjEuNzRjMC03LjYxLTYuMzMyLTEzLjMyNC0xMy44NDYtMTQuNTk0LTQuNzYtLjgwMy05LjY5Ni0xLjE2OC0xNC40My0xLjE0NnptLTE0Ljk3NiA4LjYwNWMyLjg2IDAgNS4xOTcgMi40MDcgNS4xOTcgNS4zNjcgMCAyLjk0OC0yLjMzNiA1LjMzMi01LjE5NyA1LjMzMi0yLjg3MSAwLTUuMTk2LTIuMzg0LTUuMTk2LTUuMzMxLS4wMDEtMi45NiAyLjMyNS01LjM2NyA1LjE5Ni01LjM2N3oiIGZpbGw9InVybCgjcGFpbnQwX2xpbmVhcikiLz48cGF0aCBkPSJNOTUuNjg5IDM2LjAwMXYxMi4zNzJjMCA5LjU5LTguMDY3IDE3LjY2Mi0xNy4yNjUgMTcuNjYySDUwLjgxOEM0My4yNTcgNjYuMDM1IDM3IDcyLjU1OSAzNyA4MC4xOTJ2MjYuNTI5YzAgNy41NDkgNi41MTIgMTEuOTkxIDEzLjgxOCAxNC4xNTYgOC43NDggMi41OTEgMTcuMTM4IDMuMDYxIDI3LjYwNiAwIDYuOTU3LTIuMDMyIDEzLjgxOC02LjExOCAxMy44MTgtMTQuMTU2Vjk2LjEwM0g2NC42Mzd2LTMuNTRoNDEuNDI1YzguMDMyIDAgMTEuMDI3LTUuNjQ3IDEzLjgyLTE0LjEyNCAyLjg4Ni04LjcyNyAyLjc2MS0xNy4xMiAwLTI4LjMxMy0xLjk4NS04LjA2LTUuNzc2LTE0LjEyNi0xMy44Mi0xNC4xMjZIOTUuNjg5di4wMDF6bS0xNS41MjcgNjcuMThjMi44NjYgMCA1LjE4NiAyLjM2NiA1LjE4NiA1LjI5NCAwIDIuOTM1LTIuMzIgNS4zMjQtNS4xODYgNS4zMjQtMi44NTQgMC01LjE4NS0yLjM4OS01LjE4NS01LjMyNCAwLTIuOTI4IDIuMzMtNS4yOTQgNS4xODUtNS4yOTR6IiBmaWxsPSJ1cmwoI3BhaW50MV9saW5lYXIpIi8+PGRlZnM+PGxpbmVhckdyYWRpZW50IGlkPSJwYWludDBfbGluZWFyIiB4MT0iNC4yMjQiIHkxPSI5LjM0NiIgeDI9IjY4LjgyNiIgeTI9IjYzLjY5NiIgZ3JhZGllbnRVbml0cz0idXNlclNwYWNlT25Vc2UiPjxzdG9wIHN0b3AtY29sb3I9IiM1QTlGRDQiLz48c3RvcCBvZmZzZXQ9IjEiIHN0b3AtY29sb3I9IiMzMDY5OTgiLz48L2xpbmVhckdyYWRpZW50PjxsaW5lYXJHcmFkaWVudCBpZD0icGFpbnQxX2xpbmVhciIgeDE9IjkyLjM3MyIgeTE9Ijk4LjQwNCIgeDI9IjY5LjI3MSIgeTI9IjY2LjI2NCIgZ3JhZGllbnRVbml0cz0idXNlclNwYWNlT25Vc2UiPjxzdG9wIHN0b3AtY29sb3I9IiNGRkQ0M0IiLz48c3RvcCBvZmZzZXQ9IjEiIHN0b3AtY29sb3I9IiNGRkU4NzMiLz48L2xpbmVhckdyYWRpZW50PjwvZGVmcz48L3N2Zz4=">
                                                            <br>Python
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('reactphp')" class="nav-link"
                                                            id="reactphp-tab" data-bs-toggle="tab"
                                                            data-bs-target="#reactphp-tab-pane" type="button" role="tab"
                                                            aria-controls="reactphp-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/png;base64,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">
                                                            <br>ReactPHP
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('php')" class="nav-link" id="php-tab"
                                                            data-bs-toggle="tab" data-bs-target="#php-tab-pane"
                                                            type="button" role="tab" aria-controls="php-tab-pane"
                                                            aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/png;base64,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">
                                                            <br>PHP
                                                        </button>
                                                    </li>
                                                    <li class="nav-item" role="presentation">
                                                        <button v-on:click="selectApi('rest')" class="nav-link"
                                                            id="rest-tab" data-bs-toggle="tab"
                                                            data-bs-target="#rest-tab-pane" type="button" role="tab"
                                                            aria-controls="rest-tab-pane" aria-selected="false">
                                                            <img height="32"
                                                                src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAwKSI+PGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAxKSI+PHBhdGggZD0iTTM1LjgyNyAxOS4wN2wtNC43NzgtMS4xMTNjLS4xNTQtLjM3OC0uMzA5LS43NjgtLjU0LTEuMjI0bDIuNTg4LTQuMTRhLjgwOC44MDggMCAwMC0uMDY2LS45NjdsLTMuOTY0LTMuOTVhLjc4Ljc4IDAgMDAtLjkwMy0uMTU2bC00LjE3MyAyLjU3YTUuNjg5IDUuNjg5IDAgMDAtMS4yMjItLjQ1NmwtMS4xMDEtNC43NjJhLjY4My42ODMgMCAwMC0uNDgzLS41OS42NjQuNjY0IDAgMDAtLjI2Ni0uMDIyaC01LjUwNWEuNzIyLjcyMiAwIDAwLS40OTYuMTU5LjczNi43MzYgMCAwMC0uMjYzLjQ1M2wtMS4xMDEgNC43NjJjLS4zNzUuMTU2LS43Ni4zLTEuMjEyLjUzNGwtNC4xNC0yLjYwM2EuNzg2Ljc4NiAwIDAwLS45MTMuMTFsLTMuOTg2IDMuOTVhLjk4NC45ODQgMCAwMC0uMTQzIDEuMDAybDIuNTc2IDQuMjE3YTUuMTk3IDUuMTk3IDAgMDAtLjQ1MSAxLjIyNEwuNTczIDE5LjE4MWEuNzE1LjcxNSAwIDAwLS40Mi4yNi43MjkuNzI5IDAgMDAtLjE1My40NzR2NS41NjNhLjc2Mi43NjIgMCAwMC4xNjMuNDk3Yy4xMS4xNC4yNjguMjM3LjQ0My4yNzFsNC43MTIgMS4xMTNjLjE1My40MjEuMzMuODM0LjUyOCAxLjIzNUwzLjMwMyAzMi44YS44NDIuODQyIDAgMDAuMDc3LjkyM2wzLjkwOSAzLjk5NWEuNjguNjggMCAwMC45MTQuMDc3bDQuMTczLTIuNjAzYy4zNzQuMTQ1Ljc2LjMgMS4yMS40NTZsMS4xMDIgNC43NTFhLjc0Ny43NDcgMCAwMC4yNjYuNDVjLjE0LjExMy4zMTUuMTcuNDkzLjE2Mmg1LjUwNWEuNzMzLjczMyAwIDAwLjQ5NC0uMTYyLjc0Ny43NDcgMCAwMC4yNjYtLjQ1bDEuMTAxLTQuODI5YTguOTI1IDguOTI1IDAgMDAxLjIxMS0uNTM0bDQuMTA3IDIuNjA0YS43NjUuNzY1IDAgMDAuOTAzLS4wNzhsMy45NTMtMy45ODNhLjcxNi43MTYgMCAwMC4wNzctLjkyNGwtMi41ODgtNC4yMTdjLjIyNC0uMzg3LjQwNS0uNzk3LjU0LTEuMjI0bDQuNzAxLTEuMTEyYS43MjUuNzI1IDAgMDAuNDQ4LS4yNjcuNzQuNzQgMCAwMC4xNTgtLjUwMVYxOS43N2EuNzE3LjcxNyAwIDAwLS40OTYtLjd6IiBmaWxsPSIjRDdFRkY2Ii8+PHBhdGggZD0iTTM5LjM5NCAxNS44MWwtNC43NzgtMS4xMTNjLS4xNTQtLjM3OC0uMzA4LS43NjgtLjU0LTEuMjI0bDIuNTg4LTQuMTg0YS44MDguODA4IDAgMDAtLjA3Ny0uOTIzbC0zLjk1My00LjAyOGEuOC44IDAgMDAtLjkwMy0uMTQ1TDI3LjUyNSA2LjgzYTUuMTY0IDUuMTY0IDAgMDAtMS4yMjItLjQ1NmwtMS4xLTQuNzYyYS42ODEuNjgxIDAgMDAtLjIyMy0uNDQxLjY2Ny42NjcgMCAwMC0uNDYtLjE3MWgtNS41MDVhLjcyMS43MjEgMCAwMC0uNDk2LjE1OS43MzYuNzM2IDAgMDAtLjI2NC40NTNsLTEuMTAxIDQuNzYyYTExLjU3IDExLjU3IDAgMDAtMS4yMTEuNTM0bC00LjE3My0yLjU3YS44Mi44MiAwIDAwLS45MTQgMEw2LjkwMyA4LjM2NmEuOTYyLjk2MiAwIDAwLS4xNDMuOTlsMi41NzcgNC4yMjhjLS4xOTcuMzktLjM0OC44LS40NTIgMS4yMjRsLTQuNzEyIDEuMTEzYS43MjUuNzI1IDAgMDAtLjQyMy4yMzcuNzM5LjczOSAwIDAwLS4xODMuNDUzdjUuNTYzYS43NjIuNzYyIDAgMDAuMTYzLjQ5N2MuMTExLjE0LjI2OC4yMzYuNDQzLjI3bDQuNzEyIDEuMTEzYy4xNTEuNDE5LjMyOC44MjguNTI5IDEuMjI0TDYuODI2IDI5LjU0YS44My44MyAwIDAwLjA3Ny45MTJsMy45NTMgMy45OTRhLjcwMi43MDIgMCAwMC45MTQuMDc4bDQuMTczLTIuNjE0Yy4zNzQuMTU1Ljc2LjMxMSAxLjIxLjQ2N2wxLjEwMiA0Ljc1MWEuNzQ3Ljc0NyAwIDAwLjI2Ni40NWMuMTQuMTEzLjMxNS4xNy40OTQuMTYyaDUuNTA1YS43MzMuNzMzIDAgMDAuNDkzLS4xNjIuNzQ3Ljc0NyAwIDAwLjI2Ni0uNDVsMS4xMDEtNC44MjlhOC45NTQgOC45NTQgMCAwMDEuMjEyLS41MzRsNC4yMTYgMi42MTVhLjc2NS43NjUgMCAwMC45MDMtLjA3OGwzLjk1My0zLjk4M2EuNzE2LjcxNiAwIDAwLjA3Ny0uOTI0bC0yLjYxLTQuMjI4YTUuOTcgNS45NyAwIDAwLjU0LTEuMjM1bDQuNzAxLTEuMTEzYS43MzcuNzM3IDAgMDAuNDQ2LS4yNjkuNzUuNzUgMCAwMC4xNi0uNDk4di01LjU2NGEuNzA2LjcwNiAwIDAwLS4xNy0uNDQ2LjY5Mi42OTIgMCAwMC0uNDE0LS4yMzN6bS0uODU4IDUuNzUybC00LjU0OCAxLjExMmEuNzcuNzcgMCAwMC0uNTQuNTM1IDcuOTQxIDcuOTQxIDAgMDEtLjc1OSAxLjc2OS41NC41NCAwIDAwLS4wNjYuMzExdi4wNjdhLjUzOS41MzkgMCAwMC4wNjYuMzEyYzAgLjA3OCAwIC4wNzguMDc3LjA3OGwyLjQ2NyA0LjAxNi0zLjExNiAzLjE0OS0zLjk0Mi0yLjQ1OWEuNzIxLjcyMSAwIDAwLS43NiAwIDEwLjg1IDEwLjg1IDAgMDEtMS43NS43NjguOTQ2Ljk0NiAwIDAwLS41MjkuNTQ1bC0xLjEgNC41OTVIMTkuNzNsLTEuMS00LjU5NWEuNzguNzggMCAwMC0uNTMtLjU0NWMtLjYwNC0uMjA0LTEuMTktLjQ2LTEuNzUtLjc2OGEuOTA1LjkwNSAwIDAwLS43NiAwbC00LjAxOCAyLjUzN0w4LjUgMjkuODRsMi40MzMtMy45ODNhLjY5NC42OTQgMCAwMDAtLjc2OCAxNC4yNjQgMTQuMjY0IDAgMDEtLjc2LTEuNjkxdi0uMDc4YS42NzkuNjc5IDAgMDAtLjUyOC0uNDU2TDUuMDg3IDIxLjc1di00LjQxN2w0LjU1OC0xLjExM2EuNzY5Ljc2OSAwIDAwLjUyOC0uNTM0IDcuMjA2IDcuMjA2IDAgMDEuNzYtMS43Ny44OTcuODk3IDAgMDAwLS43NjdsLTIuNTEtNC4wMTYgMy4xMTYtMy4xNSAzLjk0MSAyLjQ2YS42NzguNjc4IDAgMDAuNzYgMGMuNjE3LS4zMTIgMS4yMjItLjU0NiAxLjc1LS43NjhhLjkyMy45MjMgMCAwMC41My0uNTM0bC45OS00LjYwN2g0LjQwNGwxLjEwMSA0LjYwN2EuNzgyLjc4MiAwIDAwLjUyOS41MzQgNi44NzYgNi44NzYgMCAwMTEuNzUuNzY4Ljg3NC44NzQgMCAwMC43NiAwbDQuMDE5LTIuNTM3IDMuMTE1IDMuMTQ5LTIuNDMzIDMuOTgzYS43NC43NCAwIDAwMCAuNzY4Yy4zMDkuNjEyLjU0IDEuMjM1Ljc2IDEuNzY5YS45MjYuOTI2IDAgMDAuNTQuNTM0bDQuNTQ3IDEuMTEzdjQuMzcybC0uMDY2LS4wMzN6IiBmaWxsPSIjMDA3NEEyIi8+PHBhdGggZD0iTTE0LjkxIDE3LjY3NmwuOTkyIDIuMDc0aDEuMjVsLTEuMTI5LTIuMjU4Yy42MTQtLjI0MiAxLS44NTEgMS0xLjU4MiAwLTEuMTE3LS43NS0xLjc5Ny0xLjk3Ni0xLjc5N0gxM3Y1LjYzN2gxLjEwNXYtMi4wNzRoLjgwNXptLS44MDUtMi42NTZoLjc5N2MuNjI1IDAgLjk5My4zMzUuOTkzLjg5NCAwIC41Ny0uMzc1LjkxLTEuMDA0LjkxaC0uNzg2VjE1LjAyem03LjYxIDMuNzczaC0yLjQ0MnYtMS40NWgyLjI5N3YtLjg4NmgtMi4yOTdWMTUuMDdoMi40NDJ2LS45NTdIMTguMTZ2NS42MzdoMy41NTV2LS45NTd6bS45ODgtLjYyMWMuMDQgMS4wNTUuODQgMS42OTEgMi4xMTcgMS42OTEgMS4zMTcgMCAyLjExNC0uNjcyIDIuMTE0LTEuNzg1IDAtLjg2My0uNDc3LTEuMzUxLTEuNTY3LTEuNTk0bC0uNTc4LS4xMzJjLS42MS0uMTMzLS44Ni0uMzUyLS44Ni0uNzUgMC0uNDIyLjM0OC0uNjg0LjkwNy0uNjg0LjU1IDAgLjkwNi4yODUuOTUuNzVoMS4wNDZDMjYuNzk3IDE0LjY2IDI2LjAwNCAxNCAyNC44MjQgMTRjLTEuMjE5IDAtMS45OTYuNjY4LTEuOTk2IDEuNzE5IDAgLjgzNi41IDEuMzc1IDEuNTIgMS42MDVsLjU4Ni4xMzdjLjY1Mi4xNDUuOTAyLjM0OC45MDIuNzMgMCAuNDU3LS4zODcuNzUtLjk5Ni43NS0uNjQgMC0xLjA0LS4yOC0xLjA5LS43N2gtMS4wNDd6bTcuNjAyIDEuNTc4di00LjY4aDEuNjEzdi0uOTU3aC00LjMzNnYuOTU3aDEuNjEzdjQuNjhoMS4xMXptLTExLjc3IDdoMS4ybC0xLjY5Mi01LjYzN0gxNi43M2wtMS42OSA1LjYzN2gxLjEwOGwuMzYtMS4zNzloMS42NzZsLjM1MSAxLjM3OXptLTEuMjEtNC41MzFoLjA1bC41ODYgMi4yODVoLTEuMjNsLjU5My0yLjI4NXptMy4xMTYtMS4xMDZ2NS42MzdoMS4xMXYtMS43OWguODk4YzEuMjE1IDAgMS45ODgtLjc0NSAxLjk4OC0xLjkxNyAwLTEuMTg0LS43NS0xLjkzLTEuOTMzLTEuOTNIMjAuNDR6bTEuMTEuOTFoLjY3MmMuNzI2IDAgMS4wOS4zMzYgMS4wOSAxLjAxMiAwIC42OC0uMzY0IDEuMDEyLTEuMDkgMS4wMTJoLS42NzJ2LTIuMDI0ek0yOSAyNi43NXYtLjk1M2gtMS4xNjh2LTMuNzNIMjl2LS45NTRoLTMuNDQ1di45NTNoMS4xNjh2My43M2gtMS4xNjh2Ljk1NEgyOXoiIGZpbGw9IiMwMDc0QTIiLz48L2c+PC9nPjxkZWZzPjxjbGlwUGF0aCBpZD0iY2xpcDAiPjxwYXRoIGZpbGw9IiNmZmYiIGQ9Ik0wIDBoNDB2NDBIMHoiLz48L2NsaXBQYXRoPjxjbGlwUGF0aCBpZD0iY2xpcDEiPjxwYXRoIGZpbGw9IiNmZmYiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAgMSkiIGQ9Ik0wIDBoNDB2NDBIMHoiLz48L2NsaXBQYXRoPjwvZGVmcz48L3N2Zz4=">
                                                            <br>REST
                                                        </button>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>


                                        <!-- java -->
                                        <div v-if="selectedCode == 'java'">
                                            <java-code :endpoint="selectedEndpoint" :server="server"
                                                :token="token"></java-code>
                                        </div>

                                        <!-- nodejs -->
                                        <div v-if="selectedCode == 'nodejs'">
                                            <javascript-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></javascript-code>
                                        </div>

                                        <!-- android -->
                                        <div v-if="selectedCode == 'android'">
                                            <android-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></android-code>
                                        </div>

                                        <!-- ios -->
                                        <div v-if="selectedCode == 'ios'">
                                            <ios-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></ios-code>
                                        </div>

                                        <!-- csharp -->
                                        <div v-if="selectedCode == 'csharp'">
                                            <csharp-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></csharp-code>
                                        </div>

                                        <!-- cpp -->
                                        <div v-if="selectedCode == 'cpp'">
                                            <cpp-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></cpp-code>
                                        </div>

                                        <!-- python -->
                                        <div v-if="selectedCode == 'python'">
                                            <python-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></python-code>
                                        </div>

                                        <!-- reactphp -->
                                        <div v-if="selectedCode == 'reactphp'">
                                            <reactphp-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></reactphp-code>
                                        </div>

                                        <!-- php -->
                                        <div v-if="selectedCode == 'php'">
                                            <php-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></php-code>
                                        </div>

                                        <!-- rest -->
                                        <div v-if="selectedCode == 'rest'">
                                            <rest-code v-bind:endpoint="selectedEndpoint" v-bind:server="server"
                                                v-bind:token="token"></rest-code>
                                        </div>



                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                    </div>





    </div>

</div>
        <!-- end container-fluid -->


</template>
