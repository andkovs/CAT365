'use strict';

angular.module('myApp').filter('ordersFilter', function() {
    return function( items, airportId ) {
		var filtered = [];
		if (airportId) {
			angular.forEach(items, function(item) {
				if (item.depAirportId == airportId) {
				  	filtered.push(item);
				}
			});
		} else {
			filtered = items;
		}
		return filtered;
    };
});