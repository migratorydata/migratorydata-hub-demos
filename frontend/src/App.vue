<script setup>
import 'vue-json-pretty/lib/styles.css';

import { ref } from 'vue'

import * as data from './data'

import AppInfo from './components/AppInfo.vue'


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
    <AppInfo v-bind:endpoints="data.seismicEndpoints" v-bind:tab="data.map" v-bind:info="data.seismicInfo"
      v-bind:title="data.seismicTitle" />
  </template>
  <template v-if="selectedDemo == 'bikes'">
    <AppInfo v-bind:endpoints="data.bikesEndpoints" v-bind:tab="data.map" v-bind:info="data.bikesInfo"
      v-bind:title="data.bikesTitle" />
  </template>
  <template v-if="selectedDemo == 'parking'">
    <AppInfo v-bind:endpoints="data.parkingEndpoints" v-bind:tab="data.table" v-bind:info="data.parkingInfo"
      v-bind:title="data.parkingTitle" />
  </template>
  <template v-if="selectedDemo == 'crypto'">
    <AppInfo v-bind:endpoints="data.cryptoEndpoints" v-bind:tab="data.table" v-bind:info="data.cryptoInfo"
      v-bind:title="data.cryptoTitle" />
  </template>
  <template v-if="selectedDemo == 'traffic'">
    <AppInfo v-bind:endpoints="data.trafficEndpoints" v-bind:tab="data.table" v-bind:info="data.trafficInfo"
      v-bind:title="data.trafficTitle" />
  </template>
</template>