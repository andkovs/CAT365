'use strict';

angular.module('myApp').filter('airlinesFilter', function() {

    return function( items, airlineId ) {
        var filtered = [];
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


