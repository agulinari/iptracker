(function pollmax() {
        $.ajax({
            url: "/api/stats/max",
            type: "GET",
            success: function(data) {
                var json = "<h4>Max distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#maxdistance').html(json);
            },
            dataType: "json",
            complete: setTimeout(function() {pollmax()}, 5000),
            timeout: 2000
        })
})();

(function pollmin() {
        $.ajax({
            url: "/api/stats/min",
            type: "GET",
            success: function(data) {
                var json = "<h4>Min distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#mindistance').html(json);
            },
            dataType: "json",
            complete: setTimeout(function() {pollmin()}, 5000),
            timeout: 2000
        })
})();

(function pollavg() {
        $.ajax({
            url: "/api/stats/average",
            type: "GET",
            success: function(data) {
                var json = "<h4>Average distance</h4><pre>"
                    + JSON.stringify(data, null, 4) + "</pre>";
                $('#average').html(json);
            },
            dataType: "json",
            complete: setTimeout(function() {pollavg()}, 5000),
            timeout: 2000
        })
})()