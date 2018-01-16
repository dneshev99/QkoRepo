function showAddForm() {
    $(document).ready(function(){
        $("#addButton").click(function(){
            $("#main").load("/addElement.html");
        });
    });

}

function showSearchForm() {
    $(document).ready(function(){
        $("#searchButton").click(function(){
            $("#main").load("/search.html");
        });
    });
}