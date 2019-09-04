(function pollmax() {
    setTimeout(function() {
        $.ajax({
            url: "/api/stats/max",
            type: "GET",
            success: function(data) {
                var json = "<h4>Max distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#maxdistance').html(json);
            },
            dataType: "json",
            complete: pollmax,
            timeout: 2000
        })
    }, 5000);
})();

(function pollmin() {
    setTimeout(function() {
        $.ajax({
            url: "/api/stats/min",
            type: "GET",
            success: function(data) {
                var json = "<h4>Min distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#mindistance').html(json);
            },
            dataType: "json",
            complete: pollmin,
            timeout: 2000
        })
    }, 5000);
})();

(function pollavg() {
    setTimeout(function() {
        $.ajax({
            url: "/api/stats/average",
            type: "GET",
            success: function(data) {
                var json = "<h4>Average Distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#average').html(json);
            },
            dataType: "json",
            complete: pollavg,
            timeout: 2000
        })
    }, 5000);
})()