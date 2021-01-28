let dataExists = false;
let descriptions;
let timestamps;
let values;
let unit;
const colors = [
    'rgb(103, 135, 72)',
    'rgb(176, 114, 194)',
    'rgb(191, 159, 99)',
    'rgb(151, 191, 111)'
]
let charts;

async function loadDescriptions() {
    descriptions = await getData('http://localhost:8080/api/dataset/descriptions');
    charts = [descriptions.length];
    if(descriptions.length === 0 && !dataExists) {
        document.getElementById("no_data_found_txt").style.visibility = "visible";
        setTimeout(function(){ loadDescriptions(); }, 5000);
    } else {
        document.getElementById("no_data_found_txt").style.visibility = "hidden";
        dataExists = true;
        console.log("Fetched " + descriptions.length + " descriptions");
    }
}

async function loadData(desc) {
    timestamps = await getData('http://localhost:8080/api/dataset/timesSinceStart/' + desc);
    values = await getData('http://localhost:8080/api/dataset/values/' + desc);
    unit = await getData('http://localhost:8080/api/dataset/unit/' + desc);
    console.log("Fetched " + values.length + " values for " + desc);
}

const createChart = (ctx, i) => {
    charts[i] = new Chart(ctx, {
        // The type of chart we want to create
        type: 'line',
        // The data for our dataset
        data: {
            labels: timestamps,
            datasets: [{
                label: descriptions[i],
                backgroundColor: colors[i % colors.length],
                borderColor: colors[i % colors.length],
                data: values
            }]
        },
        // Configuration options go here
        options: {
            legend: {
                labels: {
                    fontColor: 'rgb(134, 138, 143)',
                    fontSize: 18
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        fontColor: 'rgb(134, 138, 143)',
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'value in ' + unit,
                        fontColor: 'rgb(134, 138, 143)'
                    }
                }],
                xAxes: [{
                    ticks: {
                        fontColor: 'rgb(134, 138, 143)',
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'time from start in seconds',
                        fontColor: 'rgb(134, 138, 143)'
                    }
                }]
            }
        }
    });
}

async function getData(url) {
    const response = await fetch(url);
    return response.json();
}

async function displayCharts() {
    for (let i = 0; i < descriptions.length; i++) {
        // fetch data for current descriptions
        await loadData(descriptions[i]);

        if(values.length !== 0) {
            // create canvas element in html
            let canvas = document.createElement('canvas');
            canvas.id = descriptions[i];
            document.body.appendChild(canvas);

            // create chart
            let ctx = document.getElementById(descriptions[i]).getContext('2d');
            createChart(ctx, i);
        }
    }
}

async function continuouslyUpdateCharts() {
    let length = descriptions.length;
    await loadDescriptions();
    if (length !== descriptions.length) {
        await displayCharts();
    }
    for (let i = 0; i < descriptions.length; i++) {
        // fetch data for current descriptions
        await loadData(descriptions[i]);

        if(values.length !== 0) {
            // create chart
            let ctx = document.getElementById(descriptions[i]).getContext('2d');
            createChart(ctx, i);
            charts[i].update();
        }
    }
    setTimeout(function(){ continuouslyUpdateCharts(); }, 5000);
}

async function main() {
    await loadDescriptions();
    await displayCharts();
    await continuouslyUpdateCharts();

    /*let timestamps_h = await getData('http://localhost:8080/api/dataset/timestamps/hoehe');
    let values_h = await getData('http://localhost:8080/api/dataset/values/hoehe');
    console.log("Fetched " + values_h.length + " values for height");
    let ctx = document.getElementById('height').getContext('2d');
    let chart_h = new Chart(ctx, {
        // The type of chart we want to create
        type: 'line',
        // The data for our dataset
        data: {
            labels: timestamps_h,
            datasets: [{
                label: 'Height',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: values_h
            }]
        },
        // Configuration options go here
        options: {}
    });
    let values_lf = await getData('http://localhost:8080/api/dataset/values/luftfeuchtigkeit');
    let timestamps_lf = await getData('http://localhost:8080/api/dataset/timestamps/luftfeuchtigkeit');
    console.log("Fetched " + values_lf.length + " values for humidity");
    let ctx2 = document.getElementById('humidity').getContext('2d');
    let chart_lf = new Chart(ctx2, {
        // The type of chart we want to create
        type: 'line',
        // The data for our dataset
        data: {
            labels: timestamps_lf,
            datasets: [{
                label: 'Humidity',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: values_lf
            }]
        },
        // Configuration options go here
        options: {}
    });
    let values_ld = await getData('http://localhost:8080/api/dataset/values/Luftdruck');
    let timestamps_ld = await getData('http://localhost:8080/api/dataset/timestamps/Luftdruck');
    console.log("Fetched " + values_ld.length + " values for air pressure");
    let ctx3 = document.getElementById('airpressure').getContext('2d');
    let chart_ld = new Chart(ctx3, {
        // The type of chart we want to create
        type: 'line',
        // The data for our dataset
        data: {
            labels: timestamps_ld,
            datasets: [{
                label: 'Air pressure',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: values_ld
            }]
        },
        // Configuration options go here
        options: {}
    });*/
}
