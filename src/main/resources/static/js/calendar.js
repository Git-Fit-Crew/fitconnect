"use strict";
const cal = new CalHeatMap();
const userId = document.querySelector("#user-id").value;
let today = new Date();

async function getUserWorkouts() {
	const workouts = await fetch("/workouts/" + userId).then(res => res.json());
	let data = {};
	workouts.forEach(function (e) {
		data[`${e}`] = 1;
	});
	return data;
}

getUserWorkouts().then(function (data){
	cal.init({
		start: new Date(today.getFullYear(), today.getMonth() - 4, today.getDate()),
		domain: 'month',
		subDomain: 'day',
		cellSize: 10,
		subDomainTextFormat: '',
		range: 5,
		displayLegend: false,
		cellRadius: 10,
		data: data,
		legend: [0.5],
		legendColors: ["rgba(255,255,255,0.33)", "#4bf113"],
		verticalOrientation: false
	});
})