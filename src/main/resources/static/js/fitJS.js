/*
function isLeapYear(year) {
    var resp = new Date(year,1,29).getDate()===29;;
    return resp;
}


var daysOfMonth = [31, isLeapYear((new Date().getFullYear())) ? 29: 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
var monthLetters = 'JFMAMJJASOND'.split('');


var calendarEl = document.getElementById("calendar");
var dayEl, monthEl;
var monthEl = document.createElement('div');
monthEl.className = "calendar-month";
tempEl = document.createElement('div');
tempEl.className = "calendar-month__title";
monthEl.appendChild(tempEl);

for(let i = 0; i < 31; i++) {
    dayEl = document.createElement('div');
    dayEl.className = 'calendar-day calendar-day__number';
    dayEl.textContent = i;
    monthEl.appendChild(dayEl);
}

calendarEl.appendChild(monthEl);
daysOfMonth.forEach(
    function(days, month) {
        monthEl = document.createElement('div');
        monthEl.className = "calendar-month";
        monthEl.setAttribute('data-month', month+1);
        tempEl = document.createElement('div');
        tempEl.className = "calendar-month__title";
        tempEl.textContent = monthLetters[month];
        monthEl.appendChild(tempEl);

        for(let i = 0; i < days; i++) {
            dayEl = document.createElement('div');
            dayEl.className = 'calendar-day';
            dayEl.setAttribute('data-date', i+1);
            monthEl.appendChild(dayEl);
        }
        calendarEl.appendChild(monthEl);
    }
);

*/




let modal = document.querySelector("#myModal");
if (modal != null) {
$(document).ready(function(){
    $("#myModal").modal('show');
});
}



let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)

})


$(function() {
    $("#password").click(function () {
        $("#pass-req").show("slow");
    });
});


const alertPlaceholder = document.getElementById('liveAlertPlaceholder')

const alert = (message, type) => {
    const wrapper = document.createElement('div')
    wrapper.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible" role="alert">`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('')

    alertPlaceholder.append(wrapper)
}





