'use strict';

angular.module('myApp').controller('FlightsController', ['$scope', 'dictionaryService', function ($scope, dictionaryService){

	$scope.title = "Номера рейсов";
	$scope.flights = angular.copy($scope.dictionary.flights);
	$scope.newFlight = {};
	$scope.airlines = angular.copy($scope.dictionary.airlines);
	$scope.airlines.unshift({id: 0, name: 'Все авиакомпании'});
	$scope.currentAirline = 0;

	$scope.addFlight = function (flight) {
		dictionaryService.postFlight(flight).then(function (response) {
			var newFlight = response.data;
			$scope.flights.push(newFlight);
			$scope.newFlight = {};
			updateDictionary();
		}, function (response) {
			$scope.newFlight = {};
		});
	};

	$scope.resetFlight = function (flight, form) {
		var prestineFlight = {
			number: _.findWhere($scope.dictionary.flights, { id: flight.id }).number,
			airlineId: _.findWhere($scope.dictionary.flights, { id: flight.id }).airlineId
		};
		flight.number = prestineFlight.number;
		flight.airlineId = prestineFlight.airlineId;
		form.$setPristine();
	};

	$scope.saveFlight = function (flight, form) {
		dictionaryService.putFlight(flight).then(function (response) {
			form.$setPristine();
			updateDictionary();
		}, function (response) {
		});
	};

	$scope.removeFlight = function (flight) {
		if (confirm('Вы уверены, что хотите удалить рейс ' + flight.number + '?')) {
			dictionaryService.deleteFlight(flight).then(function (response) {
				$scope.flights = _.without($scope.flights, flight);
				updateDictionary();
			}, function (response) {
			});
		}
	};

	function updateDictionary() {
		$scope.dictionary.flights = angular.copy($scope.flights);
	};

}]);