
function DynamicMap(popup, popupContent) {
    this.duration = 9000;
    this.container = popup;
    this.content = popupContent;
    this.initialized = false;
    this.onlyOneFeature = true;
    this.blinkColor = "#F6AE2D";
    this.pointNormalColor = "#F26419";
    this.textStylePoint = "bold 10px serif";
    this.featureUpdateTimes = new Map();
    this.resize = true;

    this.overlay;
    this.source;
    this.map;
    this.tileLayer;
    this.vector;
}

DynamicMap.prototype.updateMap = function(message) {
    try {
        var messagesJson = JSON.parse(message);

        this.initialize(messagesJson);

        if (this.initialized == true) {
            var simpleJson = false;
            if (Array.isArray(messagesJson)) {
                for (var i = 0; i < messagesJson.length; i++) {
                    var features = this.source.getFeatures();
                    var messageJson = messagesJson[i];
                    var jsonKeys = Object.keys(messageJson);
                    var existing = false;
                    for (var j = 0; j < features.length; j++) {
                        if (messageJson[jsonKeys[0]] == features[j].name) {
                            existing = true;

                            // update existing feature that has been modified
                            if (this.compareJsonObjects(messageJson, features[j].content) == false) {
                                this.source.removeFeature(features[j]);
                                this.addFeature(messageJson);
                            }
                        }
                    }

                    //add new feature
                    if (existing == false) {
                        this.addFeature(messageJson);
                    }
                }

                var features = this.source.getFeatures();
                for (var i = 0; i < features.length; i++) {
                    var found = false;
                    for (var j = 0; j < messagesJson.length; j++) {
                        var messageJson = messagesJson[j];
                        var jsonKeys = Object.keys(messageJson);
                        if (features[i].name == messageJson[jsonKeys[0]]) {
                            found = true;
                            break;
                        }
                    }
                    // remove feature
                    if (found == false) {
                        this.source.removeFeature(features[i]);
                    }
                }
            } else {
                // add simple feature if simple json(not list)
                var messageJson = messagesJson;
                this.addFeature(messageJson);
                simpleJson = true;
            }

            if (simpleJson == true) {
                const features = this.source.getFeatures(); 
                const feature = features[features.length - 1];
                const point = feature.getGeometry();
                const size = this.map.getSize();
                this.map.getView().setCenter(point.getCoordinates(), size);

                this.map.getView().setZoom(5);
            } else {
                if (this.resize) {
                    this.resize = false;
    
                    var extent = this.source.getExtent();
                    this.map.getView().fit(extent, this.map.getSize());    
                    this.updateSize();
                }
            }
        }

    } catch (e) {
        console.log(e);
    }
}

DynamicMap.prototype.checkIfMapShows = function(messagesJson) {
    if (Array.isArray(messagesJson)) {
        for (var i = 0; i < messagesJson.length; i++) {
            var messageJson = messagesJson[i];
            var rez = this.checkIfJsonHasLatAndLong(messageJson);
            if (rez == true) {
                return true;
            }
        }
    } else {
        var messageJson = messagesJson;
        var rez = this.checkIfJsonHasLatAndLong(messageJson);
        if (rez == true) {
            return true;
        }
    }
    return false;
}

DynamicMap.prototype.checkIfJsonHasLatAndLong = function(messageJson) {
    var lat = null;
    if (messageJson["lat"] != null) {
        lat = messageJson["lat"];
    }
    if (messageJson["lat*"] != null) {
        lat = messageJson["lat*"];
    }
    var long = null;
    if (messageJson["long"] != null) {
        long = messageJson["long"];
    }
    if (messageJson["long*"] != null) {
        long = messageJson["long*"];
    }
    if (lat != null && long != null) {
        return true;
    }
    return false;
}

DynamicMap.prototype.initialize = function(messagesJson) {
    if (this.initialized == false) {
        if (this.checkIfMapShows(messagesJson) == true) {
            this.initialized = true;
            this.updateSize();
            this.initMap();
            setInterval(() => this.blinkMap(), 500);
        }
    }
}

DynamicMap.prototype.updateSize = function() {
    setTimeout(() => {
        this.map.updateSize();
        this.map.getView().animate({
            zoom: this.map.getView().getZoom() - 1.5,
            duration: 250
        });
    }, 200);
}

DynamicMap.prototype.initMap = function() {
    this.overlay = new ol.Overlay({
        element: this.container,
        // autoPan: true,
        // autoPanAnimation: {
        //     duration: 250,
        // },
    });

    this.tileLayer = new ol.layer.Tile({
        source: new ol.source.OSM({
            wrapX: false,
        }),
    });

    this.map = new ol.Map({
        layers: [this.tileLayer],
        overlays: [this.overlay],
        target: 'map',
        view: new ol.View({
            center: [0, 0],
            zoom: 1,
        }),
    });

    this.source = new ol.source.Vector({
        wrapX: false,
    });
    this.vector = new ol.layer.Vector({
        source: this.source,
    });
    this.map.addLayer(this.vector);

    this.source.on('addfeature', function(self) {
        return function(e) {
            self.flash(e.feature);
        }
    }(this));

    this.map.on('pointermove', function(self) {
        return function(evt) {
            var coordinate = evt.coordinate;
            self.displayFeatureInfo(self.map.getEventPixel(evt.originalEvent), coordinate);
        }
    }(this));
}

DynamicMap.prototype.displayFeatureInfo = function(pixel, coordinate) {
    var feature = this.map.forEachFeatureAtPixel(pixel, function(feature) {
        return feature;
    });
    if (feature) {
        this.content.innerHTML = '';
        var jsonMessage = feature.content;
        var jsonKeys = Object.keys(jsonMessage);
        for (var i = 0; i < jsonKeys.length; i++) {
            var keyValue = jsonKeys[i];
            if (keyValue.charAt(keyValue.length - 1) != '*') {
                this.content.innerHTML += '<code>' + jsonKeys[i] + ': ' + jsonMessage[jsonKeys[i]] + '</code></br>';
            }
        }
        this.overlay.setPosition(coordinate);
    } else {
        this.overlay.setPosition(undefined);
    }
};


DynamicMap.prototype.addFeature = function(messageJson) {
    var lat = null;
    if (messageJson["lat"] != null) {
        lat = messageJson["lat"];
    }
    if (messageJson["lat*"] != null) {
        lat = messageJson["lat*"];
    }
    var long = null;
    if (messageJson["long"] != null) {
        long = messageJson["long"];
    }
    if (messageJson["long*"] != null) {
        long = messageJson["long*"];
    }
    if (lat != null && long != null) {
        var geom = new ol.geom.Point(ol.proj.fromLonLat([long, lat]));
        var feature = new ol.Feature(geom);
        var jsonKeys = Object.keys(messageJson);
        feature.name = messageJson[jsonKeys[0]];
        feature.point = "";
        for (var i = 0; i < jsonKeys.length; i++) {
            if (jsonKeys[i].charAt(0) == '&') {
                var value = messageJson[jsonKeys[i]];
                feature.point = value.toString();
                break;
            }
        }
        feature.content = messageJson;
        this.featureUpdateTimes.set(feature.name, new Date());
        this.source.addFeature(feature);
    }
}

DynamicMap.prototype.flash = function(feature) {
    if (feature.point.length != 0) {
        feature.setStyle(new ol.style.Style({
            image: new ol.style.Circle({
                radius: 10,
                stroke: new ol.style.Stroke({
                    color: '#fff',
                }),
                fill: new ol.style.Fill({
                    color: this.blinkColor,
                }),
            }),
            text: new ol.style.Text({
                text: feature.point,
                font: this.textStylePoint,
                fill: new ol.style.Fill({
                    color: '#fff',
                }),
            }),
        }));
    } else {
        feature.setStyle(new ol.style.Style({
            image: new ol.style.Circle({
                radius: 8,
                stroke: new ol.style.Stroke({
                    color: '#fff',
                }),
                fill: new ol.style.Fill({
                    color: this.blinkColor,
                }),
            }),
        }));
    }

    var start = new Date().getTime();
    var listenerKey = this.tileLayer.on('postrender', function(self) {
        return function(event) {
            var vectorContext = ol.render.getVectorContext(event);
            var frameState = event.frameState;
            var flashGeom = feature.getGeometry().clone();
            var elapsed = frameState.time - start;
            var elapsedRatio = elapsed / self.duration;
            // radius will be 5 at start and 30 at end.
            var radius = ol.easing.easeOut(elapsedRatio) * 25 + 5;
            var opacity = ol.easing.easeOut(1 - elapsedRatio);

            var style = new ol.style.Style({
                image: new ol.style.Circle({
                    radius: radius,
                    stroke: new ol.style.Stroke({
                        color: 'rgba(246, 174, 55, ' + opacity + ')',
                        width: 0.25 + opacity,
                    }),
                }),
            });

            vectorContext.setStyle(style);
            vectorContext.drawGeometry(flashGeom);
            if (elapsed > self.duration) {
                ol.Observable.unByKey(listenerKey);
                return;
            }
            // tell OpenLayers to continue postrender animation
            self.map.render();
        }
    }(this));
}

DynamicMap.prototype.compareJsonObjects = function(obj1, obj2) {
    if (Object.keys(obj1).length == Object.keys(obj2).length) {
        for (key in obj1) {
            if (obj1[key] == obj2[key]) {
                continue;
            } else {
                return false;
            }
        }
    } else {
        return false;
    }
    return true;
}

DynamicMap.prototype.blinkMap = function() {
    var now = new Date();
    var features = this.source.getFeatures();
    for (var i = 0; i < features.length; i++) {
        if (this.featureUpdateTimes.has(features[i].name)) {
            if (now - this.featureUpdateTimes.get(features[i].name) > this.duration) {
                var feature = features[i];
                if (feature.point.length != 0) {
                    feature.setStyle(new ol.style.Style({
                        image: new ol.style.Circle({
                            radius: 10,
                            stroke: new ol.style.Stroke({
                                color: '#fff',
                            }),
                            fill: new ol.style.Fill({
                                color: this.pointNormalColor,
                            }),
                        }),
                        text: new ol.style.Text({
                            text: feature.point,
                            font: this.textStylePoint,
                            fill: new ol.style.Fill({
                                color: '#fff',
                            }),
                        }),
                    }));
                } else {
                    feature.setStyle(new ol.style.Style({
                        image: new ol.style.Circle({
                            radius: 8,
                            stroke: new ol.style.Stroke({
                                color: '#fff',
                            }),
                            fill: new ol.style.Fill({
                                color: this.pointNormalColor,
                            }),
                        }),
                    }));
                }

                this.featureUpdateTimes.delete(features[i].name);
            }
        }
    }
}
