var view = new ol.View({ zoom: 9 })

var map = new ol.Map({
    target: "map",
    layers: [ new ol.layer.Tile({ source: new ol.source.OSM() })],
    controls: ol.control.defaults({
        attributionOptions: ({
            collapsible: false
        })
    }),
    view: view
});

var geolocation = new ol.Geolocation({
    projection: view.getProjection()
});

geolocation.on("error", (error) => {
    alert(error.message);
});

var positionFeature = new ol.Feature();
positionFeature.setStyle(new ol.style.Style({
    image: new ol.style.Icon({ src: "user.png", scale: 0.5 })
}));

var centerDefined = false;
geolocation.on("change:position", () => {
    var coordinates = geolocation.getPosition();
    if(!centerDefined) {
        view.setCenter(coordinates);
        centerDefined = true;
    }
    positionFeature.setGeometry(coordinates ? new ol.geom.Point(coordinates) : null);
});

new ol.layer.Vector({
    map: map, source: new ol.source.Vector({
        features: [positionFeature]
    })
});
geolocation.setTracking(true);

var msgBoxElement = document.getElementById('message-box');

var messageBox = new ol.Overlay({
    element: msgBoxElement,
    positioning: 'bottom-center'
});
map.addOverlay(messageBox);

map.on('click', (evt) =>{
    var coordinate = evt.coordinate;
    var feature = map.forEachFeatureAtPixel(evt.pixel, (feature) => feature);
    $(msgBoxElement).popover('destroy');
    if(feature) {
        messageBox.setPosition(coordinate);
        $(msgBoxElement).popover({
           'placement': 'top',
           'html': true,
           'content': feature.get('content'),
           'animation': false
        });
        $(msgBoxElement).popover('show');
    } else {
        messageBox.setPosition(coordinate);
        $(msgBoxElement).popover({
            'placement': 'top',
            'html': true,
            'title': 'New message',
            'animation': false
        }).data('bs.popover').tip().width(250).height(300)
            .append("<div id='message' style='height:70%'/>");
        $(msgBoxElement).popover('show');

        $("#message").editable((value, settings) => {
            $.ajax({
                method: 'POST',
                url: "/message",
                data: JSON.stringify({
                    content: value,
                    location: {
                        type: "Point",
                        coordinates: [coordinate[0],coordinate[1]]
                    }
                }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
            });
            messageBox.setPosition(undefined);
            return value;
        }, {
            type: "textarea",
            submit: "Save"
        })
    }
})

var vectorSource = new ol.source.Vector({
    loader: (extent, resolution, projection) => {
        var url = "/message/bbox/" + extent[0] + "," + extent[1] + "," + extent[2] + "," + extent[3];
        $.ajax({
            url: url,
            dataType: 'json',
            success: (response) => {
                if(response.error) {
                    alert(response.error.message)
                } else {
                    $.each(response, (index,value) => {
                        var feature = new ol.Feature({
                            geometry: new ol.geom.Point(value.location.coordinates),
                            content: value.content,
                            author: value.author
                        })
                        vectorSource.addFeature(feature);
                    });

                }
            }
        });
    },
    strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ({ tileSize: 512 }))
})

var vector = new ol.layer.Vector({
    source: vectorSource,
    style: new ol.style.Style({ image: new ol.style.Icon({ src: "message.png", scale: 0.5 }) })
})

map.addLayer(vector);

var eventSource = new EventSource("/message/subscribe");
eventSource.addEventListener('message', (e) => {
    var message = $.parseJSON(e.data)
    var feature = new ol.Feature({
        geometry: new ol.geom.Point(message.location.coordinates),
        content: message.content
    })
    vectorSource.addFeature(feature);
}, false);