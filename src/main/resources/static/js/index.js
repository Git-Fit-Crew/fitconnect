"use strict";
let map;
let service;
let infowindow;
const form = document.getElementById("search-form");
const noResultsDiv = document.getElementById("results-none");
const resetFormButton = document.getElementById("reset-form");
const gymInputId = "gymAddress";
let isFilteringByGym = false;

async function initMap() {
    const loggedInUser = await fetch("/loggedInUser").then(response => response.json())
    const google_maps_api = await fetch("/keys").then(response => response.json()).then(response => response.googleMapApi)

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
            }
        }
    });
}

function setGymAddress(address) {
    const form = document.getElementById("search-form");
    let gymInput = document.getElementById(gymInputId);
    if (gymInput === null) {
        gymInput = document.createElement('input');
        gymInput.setAttribute("id", gymInputId);
    }
    gymInput.setAttribute("value", address);
    gymInput.setAttribute("name", "gym");
    gymInput.setAttribute("type", "hidden");
    form.appendChild(gymInput);
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
                    <p><a href="${googleMapsUrl}" target="_blank">View on Google Maps</a></p>
                </div>`;

                infowindow.setContent(contentString);
                infowindow.open(map, marker);
                setGymAddress(place.vicinity);
                document.getElementById("radius").disabled = true;
                isFilteringByGym = true;
                filterUsersList().then();
            }
        });
    });
}

function getFilterParams(elements) {
    const params = new URLSearchParams();
    elements.forEach(function (element) {
        if (element.value.length === 0 || (element.type === 'checkbox' && !element.checked)) {
            return
        }
        if (element.type === 'checkbox' && element.checked) {
            console.log(element);
            params.append(element.getAttribute("name"), element.value)
            return
        }
        params.set(element.getAttribute("name"), element.value);
    });
    console.log(Object.fromEntries(params));
    return params;
}

function showLoadingSpinner() {
    form.querySelectorAll("select").forEach((e) => e.disabled = true);
    noResultsDiv.classList.add("d-none");
    document.querySelectorAll(".user-result").forEach((e) => e.classList.add("d-none"));
    document.getElementById("results-spinner").classList.remove("d-none");
}

function hideLoadingSpinner() {
    form.querySelectorAll("select").forEach((e) => {
        if (e.id === "radius" && isFilteringByGym) {
            return
        }
        e.disabled = false;
    });
    document.getElementById("results-spinner").classList.add("d-none");
}

async function filterUsersList() {
    //do loading feedback here
    showLoadingSpinner();
    //fetch list of users from filter
    let users = await fetch("/filter?" + getFilterParams(form.querySelectorAll("input, select"))).then(res => res.json());
    hideLoadingSpinner();
    if (users.length === 0) {
        noResultsDiv.classList.remove("d-none");
        return
    }
    //hide users not on the list
    for (const user of users) {
        const userCard = document.querySelector(`[data-user-id='${user.id}']`);
        userCard.classList.remove("d-none");
    }
}

form.addEventListener("submit", async function (e) {
    e.preventDefault();
    await filterUsersList();
});

form.querySelectorAll("input, select").forEach(function (el) {
    el.addEventListener("change", filterUsersList)
});

resetFormButton.addEventListener("click", function () {
    document.getElementById("radius").disabled = false;
    isFilteringByGym = false;
    form.querySelectorAll("input, select").forEach(function (el) {
        if (el.tagName === 'select') {
            el.selectedIndex = 0;
        }
        if (el.id === gymInputId) {
            el.remove();
        }
        document.querySelectorAll(".user-result").forEach((e) => e.classList.remove("d-none"));
    });

});

window.initMap = initMap;







