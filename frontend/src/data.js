export const map = 'map'
export const table = 'table'

export const seismicTitle = "Latest Earthquakes"
export const seismicInfo = "This streaming API delivers real-time updates on earthquakes worldwide from the Euro-Mediterranean Seismological Centre. It offers a single endpoint to get the latest earthquakes, including their location on the globe, magnitude, and depth."
export const seismicEndpoints = [
  {
    endpoint: "/migratorydata/seismic/info",
    operation: "SUB",
  }
]

export const bikesTitle = "Available bikes"
export const bikesInfo = "This streaming API provides real-time data on the availability of bikes in a number of cities across various countries. The information is gathered from JCDecaux.com."
export const bikesEndpoints = [
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

export const parkingTitle = "Car parking places"
export const parkingInfo = "This streaming API supplies real-time information about the available car spaces. Data is sourced from data.europa.eu. At this moment, the API offers two endpoints delivering real-time parking data for Amsterdam and Zurich."
export const parkingEndpoints = [
  {
    endpoint: "/migratorydata/parking/amsterdam",
    operation: "SUB",
  },
  {
    endpoint: "/migratorydata/parking/zurich",
    operation: "SUB",
  }
]

export const cryptoTitle = "Cryptocurrency prices"
export const cryptoInfo = "This streaming API provides real-time data about a number of cryptocurrencies. Price data is retrieved from alternative.me. "
export const cryptoEndpoints = [
  {
    endpoint: "/migratorydata/cryptocurrency/rates",
    operation: "SUB",
  }
]

export const trafficTitle = "Live traffic"
export const trafficInfo = "This streaming API supplies real-time traffic details, acquired through magnetic loops or cameras. Data is sourced from data.europa.eu. At this moment, the API offers a single endpoint delivering real-time traffic data for the Brussels Region."
export const trafficEndpoints = [
  {
    endpoint: "/migratorydata/traffic/brussels",
    operation: "SUB",
  }
]
