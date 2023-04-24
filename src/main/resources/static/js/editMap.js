let map;
let service;
let infowindow;
let homeGymMarker = null;

async function initMap() {
    const loggedInUser = await fetch("/loggedInUser").then(response => response.json())
    const google_maps_api = await fetch("/keys").then(response => response.json()).then(response => response.googleMapApi)

    console.log(loggedInUser.zipcode)

    let defaultCenter;

    if (loggedInUser.zipcode) {
        const zipCodeData = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${loggedInUser.zipcode}&key=${google_maps_api}`).then((response) => response.json());

        if (zipCodeData.results && zipCodeData.results[0].geometry) {
            defaultCenter = new google.maps.LatLng(
                zipCodeData.results[0].geometry.location.lat,
                zipCodeData.results[0].geometry.location.lng
            );
        } else {
            defaultCenter = new google.maps.LatLng(29.424122, -98.493629);
        }
    } else {
        defaultCenter = new google.maps.LatLng(29.424122, -98.493629);
    }

    infowindow = new google.maps.InfoWindow();
    map = new google.maps.Map(document.getElementById("map"), {
        center: defaultCenter,
        zoom: 15,
        styles: [
            {elementType: "geometry", stylers: [{color: "#242f3e"}]},
            {elementType: "labels.text.stroke", stylers: [{color: "#242f3e"}]},
            {elementType: "labels.text.fill", stylers: [{color: "#746855"}]},
            {
                featureType: "administrative.locality",
                elementType: "labels.text.fill",
                stylers: [{color: "#d59563"}],
            },
            {
                featureType: "poi",
                elementType: "labels.text.fill",
                stylers: [{color: "#d59563"}],
            },
            {
                featureType: "poi.park",
                elementType: "geometry",
                stylers: [{color: "#263c3f"}],
            },
            {
                featureType: "poi.park",
                elementType: "labels.text.fill",
                stylers: [{color: "#6b9a76"}],
            },
            {
                featureType: "road",
                elementType: "geometry",
                stylers: [{color: "#38414e"}],
            },
            {
                featureType: "road",
                elementType: "geometry.stroke",
                stylers: [{color: "#212a37"}],
            },
            {
                featureType: "road",
                elementType: "labels.text.fill",
                stylers: [{color: "#9ca5b3"}],
            },
            {
                featureType: "road.highway",
                elementType: "geometry",
                stylers: [{color: "#746855"}],
            },
            {
                featureType: "road.highway",
                elementType: "geometry.stroke",
                stylers: [{color: "#1f2835"}],
            },
            {
                featureType: "road.highway",
                elementType: "labels.text.fill",
                stylers: [{color: "#f3d19c"}],
            },
            {
                featureType: "transit",
                elementType: "geometry",
                stylers: [{color: "#2f3948"}],
            },
            {
                featureType: "transit.station",
                elementType: "labels.text.fill",
                stylers: [{color: "#d59563"}],
            },
            {
                featureType: "water",
                elementType: "geometry",
                stylers: [{color: "#17263c"}],
            },
            {
                featureType: "water",
                elementType: "labels.text.fill",
                stylers: [{color: "#515c6d"}],
            },
            {
                featureType: "water",
                elementType: "labels.text.stroke",
                stylers: [{color: "#17263c"}],
            },
        ],
    });

    service = new google.maps.places.PlacesService(map);

    // Add the search box
    const input = document.getElementById("search-box");
    const searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    // Bias the SearchBox results towards current map's viewport.
    map.addListener("bounds_changed", () => {
        searchBox.setBounds(map.getBounds());
    });

    searchBox.addListener("places_changed", () => {
        const places = searchBox.getPlaces();

        if (places.length === 0) {
            return;
        }

        // For each place, get the icon, name, and location.
        const bounds = new google.maps.LatLngBounds();
        places.forEach((place) => {
            if (!place.geometry || !place.geometry.location) {
                console.log("Returned place contains no geometry");
                return;
            }

            // Check if the home gym marker is present and if its position is not the same as the new search location.
            if (!homeGymMarker || !homeGymMarker.getPosition().equals(place.geometry.location)) {
                // Update the map bounds to include the new search location.
                bounds.extend(place.geometry.location);
            }
        });

        // Fit the map bounds to include the new search location.
        map.fitBounds(bounds);
    });

    google.maps.event.addListener(map, "idle", searchGyms);
}

function searchGyms() {
    const request = {
        bounds: map.getBounds(),
        keyword: "gym",
    };

    service.nearbySearch(request, (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK && results) {
            for (let i = 0; i < results.length; i++) {
                createMarker(results[i]);
            }
        }
    });
}

async function addHomeGym(name, address, marker) {
    const response = await fetch('/gyms?name=' + name + '&address=' + address, {
        method: 'GET',
    });

    window.alert('Home gym has been changed');

    if (homeGymMarker) {
        homeGymMarker.setIcon("/img/anyGym16.png");
    }

    marker.setIcon("/img/heartHomeGym1.png");
    homeGymMarker = marker;
}


function createMarker(place) {
    if (!place.geometry || !place.geometry.location) return;

    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
        icon: "/img/anyGym16.png",
    });

    google.maps.event.addListener(marker, "click", () => {
        service.getDetails({placeId: place.place_id, fields: ['opening_hours']}, (placeDetails, status) => {
            if (status === google.maps.places.PlacesServiceStatus.OK) {
                const hours = placeDetails.opening_hours ? placeDetails.opening_hours.weekday_text.join('<br>') : 'Hours not available';
                const googleMapsUrl = `https://www.google.com/maps/place/?q=place_id:${place.place_id}`;

                const contentString = `<div class="info-window-content">
                <h2>${place.name}</h2>
                <p>Address: ${place.vicinity}</p>
                <p>Rating: ${place.rating}</p>
                <p>Hours:<br>${hours}</p>
                ${
                    place.photos
                        ? `<img src="${place.photos[0].getUrl({maxWidth: 200, maxHeight: 200})}" alt="${place.name}">`
                        : ""
                }
                <p><button id="home-gym-button">Make this my home gym</button></p>
            </div>`;

                infowindow.setContent(contentString);
                infowindow.open(map, marker);

                google.maps.event.addListenerOnce(infowindow, 'domready', () => {
                    document.getElementById('home-gym-button').addEventListener('click', () => {
                        addHomeGym(place.name, place.vicinity, marker);
                    });
                });
            }
        });
    });
}

window.initMap = initMap;
