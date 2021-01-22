async function getData(url) {
    const response = await fetch(url);
    return response.json();
}

async function main() {
    let timestamps_h = await getData('http://localhost:8080/api/dataset/timestamps/hoehe');
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
    });
}
