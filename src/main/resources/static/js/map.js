// Load the Google Maps JavaScript API and Places library
function initMap() {
    const map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 29.4241, lng: -98.4936 }, // San Antonio coordinates
        zoom: 12,
    });

    const placesService = new google.maps.places.PlacesService(map);

    // Create a search box and link it to the UI element
    const input = document.getElementById("search-box");
    const searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    // Bias the search box results towards the map's viewport
    map.addListener("bounds_changed", () => {
        searchBox.setBounds(map.getBounds());
    });

    // Perform a search when the user types in a location
    searchBox.addListener("places_changed", () => {
        const places = searchBox.getPlaces();

        if (places.length === 0) {
            return;
        }

        // Clear out any existing markers on the map
        markers.forEach((marker) => {
            marker.setMap(null);
        });
        markers = [];

        // For each place, create a new marker and display it on the map
        const bounds = new google.maps.LatLngBounds();
        places.forEach((place) => {
            if (!place.geometry || !place.geometry.location) {
                console.log("Returned place contains no geometry");
                return;
            }

            const marker = new google.maps.Marker({
                map,
                title: place.name,
                position: place.geometry.location,
            });
            markers.push(marker);

            if (place.geometry.viewport) {
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }
        });

        // Set the bounds of the map to fit all the markers
        map.fitBounds(bounds);
    });
}
