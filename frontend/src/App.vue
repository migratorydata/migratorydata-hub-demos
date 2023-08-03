<script setup>
import 'vue-json-pretty/lib/styles.css';

import { ref } from 'vue'

import AppInfo from './components/AppInfo.vue'

const map = 'map'
const table = 'table'

const seismicTitle = "Latest Earthquakes"
const seismicInfo = "This streaming API delivers real-time updates on earthquakes worldwide from the Euro-Mediterranean Seismological Centre. It offers a single endpoint to get the latest earthquakes, including their location on the globe, magnitude, and depth."
const seismicEndpoints = [
  {
    endpoint: "/migratorydata/seismic/info",
    operation: "SUB",
  }
]

const bikesTitle = "Available bikes"
const bikesInfo = "This streaming API provides real-time data on the availability of bikes in a number of cities across various countries. The information is gathered from JCDecaux.com."
const bikesEndpoints = [
  {
    endpoint: "/migratorydata/jcdecauxbikes/santander",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/amiens",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/lillestrom",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/namur",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/toyama",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/besancon",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/nancy",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/jcdecauxbikes/vilnius",
    operation: "SUB",
  }
]

const parkingTitle = "Car parking places"
const parkingInfo = "This streaming API supplies real-time information about the available car spaces. Data is sourced from data.europa.eu. At this moment, the API offers two endpoints delivering real-time parking data for Amsterdam and Zurich."
const parkingEndpoints = [
  {
    endpoint: "/migratorydata/parking/amsterdam",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/parking/zurich",
    operation: "SUB",
  }
]

const cryptoTitle = "Cryptocurrency prices"
const cryptoInfo = "This streaming API provides real-time data about a number of cryptocurrencies. Price data is retrieved from alternative.me. "
const cryptoEndpoints = [
  {
    endpoint: "/migratorydata/cryptocurrency/rates",
    operation: "SUB",
  }
]

const trafficTitle = "Live traffic"
const trafficInfo = "This streaming API supplies real-time traffic details, acquired through magnetic loops or cameras. Data is sourced from data.europa.eu. At this moment, the API offers a single endpoint delivering real-time traffic data for the Brussels Region."
const trafficEndpoints = [
  {
    endpoint: "/migratorydata/traffic/brussels",
    operation: "SUB",
  }
]

const selectedDemo = ref('seismic')

function selectDemo(api) {
  selectedDemo.value = api;
}

</script>

<template>
  <div class="container">


    <nav class="raw navbar navbar-expand-md navbar-light p-3 rounded-bottom mb-2 shadow-sm">
      <a class="navbar-brand px-n4" href="#"><img src="https://migratorydata.com/images/migratorydata-logo.png"
          style="width:220px"></a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"><span class="navbar-toggler-icon"></span></button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <!-- Left Side Of Navbar -->
        <ul class="navbar-nav me-auto">
          <li class="nav-item active">
            <a class="nav-link" @click="selectDemo('seismic')" href="#">Earthquakes<span
                class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" @click="selectDemo('bikes')" href="#">Bikes</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" @click="selectDemo('parking')" href="#">Parking</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" @click="selectDemo('crypto')" href="#">Cryptocurrencies</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" @click="selectDemo('traffic')" href="#">Live Traffic</a>
          </li>
        </ul>



      </div>
    </nav>



  </div>
  <template v-if="selectedDemo == 'seismic'">
    <AppInfo v-bind:endpoints="seismicEndpoints" v-bind:tab="map" v-bind:info="seismicInfo" v-bind:title="seismicTitle" />
  </template>
  <template v-if="selectedDemo == 'bikes'">
    <AppInfo v-bind:endpoints="bikesEndpoints" v-bind:tab="map" v-bind:info="bikesInfo" v-bind:title="bikesTitle"/>
  </template>
  <template v-if="selectedDemo == 'parking'">
    <AppInfo v-bind:endpoints="parkingEndpoints" v-bind:tab="table" v-bind:info="parkingInfo" v-bind:title="parkingTitle"/>
  </template>
  <template v-if="selectedDemo == 'crypto'">
    <AppInfo v-bind:endpoints="cryptoEndpoints" v-bind:tab="table" v-bind:info="cryptoInfo" v-bind:title="cryptoTitle"/>
  </template>
  <template v-if="selectedDemo == 'traffic'">
    <AppInfo v-bind:endpoints="trafficEndpoints" v-bind:tab="table" v-bind:info="trafficInfo" v-bind:title="trafficTitle"/>
  </template>
</template>