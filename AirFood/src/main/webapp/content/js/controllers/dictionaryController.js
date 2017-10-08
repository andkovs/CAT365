'use strict';

angular.module('myApp').controller('DictionaryController', ['dictionary', '$state', '$scope', function (dictionary, $state, $scope) {
	$scope.title = "Справочники";
	$scope.$state = $state;
	$scope.dictionary = dictionary.data;
	console.log('$scope.dictionary', $scope.dictionary);
}]);