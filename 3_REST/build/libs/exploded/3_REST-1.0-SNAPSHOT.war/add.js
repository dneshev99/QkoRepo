function createCamera() {
    var camera = {};
    var lens = {};
    var display = {};


    lens["manufacturer"] = $("#lensManufacturer").val();
    lens["model"] = $("#lensModel").val();
    lens["focalLength"] = $("#focalLength").val();
    lens["maximumAperture"] = $("#maximumAperture").val();
    lens["minimumAperture"] = $("#minimumAperture").val();
    lens["filerSize"] = $("#filerSize").val();

    display["type"] = $("#type").val();
    display["size"] = $("#size").val();
    display["dots"] = $("#dots").val();

    camera["manufacturer"] = $("#manufacturer").val();
    camera["model"] = $("#model").val();
    camera["megapixels"] = $("#megapixels").val();
    camera["minISO"] = $("#minISO").val();
    camera["maxISO"] = $("#maxISO").val();
    camera["shootingSpeed"] = $("#shootingSpeed").val();
    camera["videoRecording"] = $("#videoRecording").val();
    camera["afPoints"] = $("#afPoints").val();
    camera["crossTypeAFPoints"] = $("#crossTypeAFPoints").val();
    camera["lens"] = lens;
    camera["display"] = display;
    camera["price"] = $("#price").val();


    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/camera",
        data: JSON.stringify(camera),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){alert("Record saved successfully.");},
        failure: function(errMsg) {
            alert(errMsg);
        }
    });

}
