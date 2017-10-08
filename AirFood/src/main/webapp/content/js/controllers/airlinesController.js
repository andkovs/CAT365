'use strict';

angular.module('myApp').controller('AirlinesController', ['$scope', 'dictionaryService', function ($scope, dictionaryService){

	$scope.title = "Авиакомпании";
	$scope.airlines = angular.copy($scope.dictionary.airlines);
	$scope.newAirline = {};

	$scope.addAirline = function (airline) {
		dictionaryService.postAirline(airline).then(function (response) {
			var newAirline = response.data;
			$scope.airlines.push(newAirline);
			$scope.newAirline = {};
			updateDictionary();
		}, function (response) {
			$scope.newAirline = {};
		});
	};

	$scope.resetAirline = function (airline, form) {
		airline.name = _.findWhere($scope.dictionary.airlines, { id: airline.id }).name;
		airline.fullname = _.findWhere($scope.dictionary.airlines, { id: airline.id }).fullname;
		airline.iata = _.findWhere($scope.dictionary.airlines, { id: airline.id }).iata;
		airline.phone = _.findWhere($scope.dictionary.airlines, { id: airline.id }).phone;
		airline.email = _.findWhere($scope.dictionary.airlines, { id: airline.id }).email;
		form.$setPristine();
	};

	$scope.saveAirline = function (airline, form) {
		dictionaryService.putAirline(airline).then(function () {
			form.$setPristine();
			updateDictionary();
		}, function () {
		});
	};

	$scope.removeAirline = function (airline) {
		if (confirm('Вы уверены, что хотите удалить авиакомпанию ' + airline.name + '? Это приведет к удалению всех бортов этой авиакомпании!')) {
			dictionaryService.deleteAirline(airline).then(function (response) {
				$scope.airlines = _.without($scope.airlines, airline);
				updateDictionary();
			}, function (response) {
			});
		}
	};

	function updateDictionary() {
		$scope.dictionary.airlines = angular.copy($scope.airlines);
	};
}]);