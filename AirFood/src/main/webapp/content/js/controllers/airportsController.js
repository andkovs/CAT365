'use strict';

angular.module('myApp').controller('AirportsController', ['$scope', 'dictionaryService', function ($scope, dictionaryService){

	$scope.title = "Аэропорты";
	$scope.airports = angular.copy($scope.dictionary.airports);
	$scope.newAirport = {};

	$scope.addAirport = function (airport) {
		dictionaryService.postAirport(airport).then(function (response) {
			var newAirport = response.data;
			$scope.airports.push(newAirport);
			$scope.newAirport = {};
			updateDictionary();
		}, function (response) {
			$scope.newAirport = {};
		});
	};

	$scope.resetAirport = function (airport, form) {
		airport.name = _.findWhere($scope.dictionary.airports, { id: airport.id }).name;
		form.$setPristine();
	};

	$scope.saveAirport = function (airport, form) {
		dictionaryService.putAirport(airport).then(function (response) {
			form.$setPristine();
			updateDictionary();
		}, function (response) {
		});
	};

	$scope.removeAirport = function (airport) {
		if (confirm('Вы уверены, что хотите удалить аэропорт ' + airport.name + '?')) {
			dictionaryService.deleteAirport(airport).then(function (response) {
				$scope.airports = _.without($scope.airports, airport);
				updateDictionary();
			}, function (response) {
			});
		}
	};

	function updateDictionary() {
		$scope.dictionary.airports = angular.copy($scope.airports);
	};
}]);