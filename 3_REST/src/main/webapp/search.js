function byFields() {
    alert("DA")
    var params = {};
    var table = "<table>";

    table.append("<tr>"
        +"<td>"+"Manufacturer"+"</td>"
        +"<td>"+"Model"+"</td>"
        +"<td>"+"Megapixels"+"</td>"
        +"<td>"+"MinISO"+"</td>"
        +"<td>"+"MaxISO"+"</td>"
        +"<td>"+"Shooting Speed"+"</td>"
        +"<td>"+"Video Recording"+"</td>"
        +"<td>"+"AF Points"+"</td>"
        +"<td>"+"Cross Type AF Points"+"</td>"
        +"<td>"+"Price"+"</td>"
        +"</tr>");

    $(document).ready(function(){
        $(".input").each(function () {
            if(this.val()!== '') {
                params[this.attr("id")] = this.val();
            }
        })
    });


    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/camera/searchByField/",
        data : params,
        success: function(data){
            alert("Da");
            /*$.each(data,function(i,item){
                alert("Da");
                table.append(
                    "<tr>"
                    +"<td>"+item.manufacturer+"</td>"
                    +"<td>"+item.model+"</td>"
                    +"<td>"+item.megapixels+"</td>"
                    +"<td>"+item.minISO+"</td>"
                    +"<td>"+item.maxISO+"</td>"
                    +"<td>"+item.shootingSpeed+"</td>"
                    +"<td>"+item.videoRecording+"</td>"
                    +"<td>"+item.afPoints+"</td>"
                    +"<td>"+item.crossTypeAFPoints+"</td>"
                    +"<td>"+item.price+"</td>"
                    +"</tr>" );
            })*/
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });
}

function allData() {
    var table = "<table>";

    table.append("<tr>"
        +"<td>"+"Manufacturer"+"</td>"
        +"<td>"+"Model"+"</td>"
        +"<td>"+"Megapixels"+"</td>"
        +"<td>"+"MinISO"+"</td>"
        +"<td>"+"MaxISO"+"</td>"
        +"<td>"+"Shooting Speed"+"</td>"
        +"<td>"+"Video Recording"+"</td>"
        +"<td>"+"AF Points"+"</td>"
        +"<td>"+"Cross Type AF Points"+"</td>"
        +"<td>"+"Price"+"</td>"
        +"</tr>");



    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/camera/all/",
        success: function(data){
            $.each(data,function(i,item){
                alert("Da");
                table.append(
                    "<tr>"
                    +"<td>"+item.manufacturer+"</td>"
                    +"<td>"+item.model+"</td>"
                    +"<td>"+item.megapixels+"</td>"
                    +"<td>"+item.minISO+"</td>"
                    +"<td>"+item.maxISO+"</td>"
                    +"<td>"+item.shootingSpeed+"</td>"
                    +"<td>"+item.videoRecording+"</td>"
                    +"<td>"+item.afPoints+"</td>"
                    +"<td>"+item.crossTypeAFPoints+"</td>"
                    +"<td>"+item.price+"</td>"
                    +"</tr>" );
            })
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });

    table.append("</table>");

    $("#table").parseHTML(table);
}