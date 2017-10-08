'use strict';

angular.module('myApp').controller('BoardsController', ['$scope', 'dictionaryService', function ($scope, dictionaryService){

	$scope.title = "Бортовые номера";
	$scope.boards = angular.copy($scope.dictionary.boards);
	$scope.newBoard = {};
	$scope.airlines = angular.copy($scope.dictionary.airlines);
	$scope.airlines.unshift({id: 0, name: 'Все авиакомпании'});
	$scope.currentAirline = 0;

	$scope.addBoard = function (board) {
		dictionaryService.postBoard(board).then(function (response) {
			var newBoard = response.data;
			$scope.boards.push(newBoard);
			$scope.newBoard = {};
			updateDictionary();
		}, function (response) {
			$scope.newBoard = {};
		});
	};

	$scope.resetBoard = function (board, form) {
		var prestineBoard = {
			number: _.findWhere($scope.dictionary.boards, { id: board.id }).number,
			aircraft: _.findWhere($scope.dictionary.boards, { id: board.id }).aircraft,
			airlineId: _.findWhere($scope.dictionary.boards, { id: board.id }).airlineId
		};
		board.number = prestineBoard.number;
		board.aircraft = prestineBoard.aircraft;
		board.airlineId = prestineBoard.airlineId;
		form.$setPristine();
	};

	$scope.saveBoard = function (board, form) {
		dictionaryService.putBoard(board).then(function (response) {
			form.$setPristine();
			updateDictionary();
		}, function (response) {
		});
	};

	$scope.removeBoard = function (board) {
		if (confirm('Вы уверены, что хотите удалить борт ' + board.number + '?')) {
			dictionaryService.deleteBoard(board).then(function (response) {
				$scope.boards = _.without($scope.boards, board);
				updateDictionary();
			}, function (response) {
			});
		}
	};

	function updateDictionary() {
		$scope.dictionary.boards = angular.copy($scope.boards);
	};
}]);