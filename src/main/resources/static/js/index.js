let map;
let service;
let infowindow;

async function initMap() {

    const loggedInUser = await fetch("/loggedInUser").then(response => response.json())
    const google_maps_api = await fetch("/keys").then(response => response.json()).then(response => response.googleMapApi)

    console.log(loggedInUser.zipcode)

    let defaultCenter;

    // Check if the user has a zip code
    if (loggedInUser.zipcode) {
        // Use the user's zip code to get the latitude and longitude
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


    // Search for gyms when the map is moved and idle
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
                saveGymInfo(results[i]); // Add this line to save gym information
            }
        }
    });
}


async function saveGymInfo(place) {
    if (!place.geometry || !place.geometry.location) return;
    console.log(place);
    const gymData = {
        name: place.name,
        address: place.vicinity,
        // Add other gym properties if needed
    };

    const response = await fetch('/gyms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(gymData),
    });

    return response.json();
}

function createMarker(place) {
    if (!place.geometry || !place.geometry.location) return;

    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,

    });

    // Save the gym location data
    // saveGymLocation(place.name, place.vicinity);

    google.maps.event.addListener(marker, "click", () => {
        service.getDetails({ placeId: place.place_id, fields: ['opening_hours'] }, (placeDetails, status) => {
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
                        ? `<img src="${place.photos[0].getUrl({ maxWidth: 200, maxHeight: 200 })}" alt="${place.name}">`
                        : ""
                }
                    <p><a href="${googleMapsUrl}" target="_blank">View on Google Maps</a></p>
                </div>`;

                infowindow.setContent(contentString);
                infowindow.open(map, marker);
            }
        });
    });
}

window.initMap = initMap;




