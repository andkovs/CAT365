'use strict';

angular.module('myApp').filter('flightsFilter', function() {
    return function( items, airlineId ) {
        var filtered = [null];
        //var filtered = [];
        if (airlineId) {
            angular.forEach(items, function(item) {
                if (item.airlineId == airlineId) {
                    filtered.push(item);
                }
            });
        } else {
            filtered = items;
        }
        return filtered;
    };
});


