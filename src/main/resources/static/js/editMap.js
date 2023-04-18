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
    });

    service = new google.maps.places.PlacesService(map);

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

    marker.setIcon("/img/gym-icon.png");

    if (homeGymMarker) {
        homeGymMarker.setIcon(null);
    }

    homeGymMarker = marker;
}

function createMarker(place) {
    if (!place.geometry || !place.geometry.location) return;

    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
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
