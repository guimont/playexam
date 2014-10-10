
/**
* init stage kinetic canvas
* init route for get parameter data
* init websocket for new notification*/

var stageChart;
var layerChart;

function load () {

    stageChart = new Kinetic.Stage({
        container: "kinetic",
        width: 200,
        height: 120
    });

    layerChart = new Kinetic.Layer();

    clockChanged(time);
};

/**
 * onload javascript function
 */
window.onload = function() {
    load();
};




function timecount(layer, degS, degM) {
    var group = new Kinetic.Group();
    group.add(new Kinetic.Arc({
        x: 90,
        y: 60,
        innerRadius: 40,
        outerRadius: 42,
        fill: '#428bca',
        strokeWidth: 0,
        lineJoin: "round",
        angle: degS,
        rotationDeg: -90
    }));

    group.add(new Kinetic.Arc({
        x: 90,
        y: 60,
        innerRadius: 40,
        outerRadius: 42,
        fill: '#ddd',
        strokeWidth: 0,
        lineJoin: "round",
        angle: 360 - degS,
        rotationDeg: -90 + degS
    }));

    group.add(new Kinetic.Arc({
        x: 90,
        y: 60,
        innerRadius: 45,
        outerRadius: 49,
        fill: '#428bca',
        strokeWidth: 0,
        lineJoin: "round",
        angle: degM,
        rotationDeg: -90
    }));

    group.add(new Kinetic.Arc({
        x: 90,
        y: 60,
        innerRadius: 45,
        outerRadius: 49,
        fill: '#ddd',
        strokeWidth: 0,
        lineJoin: "round",
        angle: 360 - degM,
        rotationDeg: -90 + degM
    }));

    layer.add(group);
}


var clockChanged = function(time) {
    if (layerChart) {
        var degS = String(time).slice(2,4)*360/60;
        var degM = String(time).slice(0,2)*360/30;
        layerChart.destroyChildren();
        timecount(layerChart,degS,degM);
        stageChart.add(layerChart);
    }

}