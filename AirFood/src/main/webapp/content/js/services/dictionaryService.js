'use strict';

angular.module('myApp').service('dictionaryService', ['$http', function ($http) {

	// Get dictionary
	this.get = function () {
		return $http.get('rest/dictionary', {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Get airports
	this.getAirports = function () {
		return $http.get('rest/airports', {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Get airports
	this.getAirportsByUserId = function () {
		return $http.get('rest/airports/user', {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Post airport
	this.postAirport = function (airport) {
		return $http.post('rest/airports', airport, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Put airport
	this.putAirport = function (airport) {
		//var path = baseUrl.dev + '';
		return $http.put('rest/airports/' + airport.id, airport, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Delete airport
	this.deleteAirport = function (airport) {
		//var path = baseUrl.dev + '';
		return $http.delete('rest/airports/' + airport.id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Post airline
	this.postAirline = function (airline) {
		//var path = baseUrl.dev + '';
		return $http.post('rest/airlines', airline, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Put airline
	this.putAirline = function (airline) {
		//var path = baseUrl.dev + '';
		return $http.put('rest/airlines/' + airline.id, airline, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Delete airline
	this.deleteAirline = function (airline) {
		//var path = baseUrl.dev + '';
		return $http.delete('rest/airlines/' + airline.id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Post board
	this.postBoard = function (board) {
		//var path = baseUrl.dev + '';
		return $http.post('rest/boards', board, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Put board
	this.putBoard = function (board) {
		//var path = baseUrl.dev + '';
		return $http.put('rest/boards/' + board.id, board, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Delete board
	this.deleteBoard = function (board) {
		//var path = baseUrl.dev + '';
		return $http.delete('rest/boards/' + board.id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Post flight
	this.postFlight = function (flight) {
		//var path = baseUrl.dev + '';
		return $http.post('rest/flights', flight, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Put flight
	this.putFlight = function (flight) {
		//var path = baseUrl.dev + '';
		return $http.put('rest/flights/' + flight.id, flight, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Delete flight
	this.deleteFlight = function (flight) {
		//var path = baseUrl.dev + '';
		return $http.delete('rest/flights/' + flight.id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};
}]);