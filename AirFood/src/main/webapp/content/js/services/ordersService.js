'use strict';

angular.module('myApp').service('ordersService', ['$http', function ($http) {

	// Get orders
	this.get = function (from, to) {
		console.log('orders from to', from, to);
		return $http.get('rest/orders', {
			params: {from: from, to: to},
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Get orders/id
	this.getDetails = function(id) {
		return $http.get('rest/orders/'+id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Get order excel
	this.getOrderExcel = function (id) {
		$http({
			url: 'rest/files/excel/'+id,
			method: "GET",
			responseType: 'arraybuffer',
			headers: {'token': sessionStorage.getItem('token')}
		}).success(function (data, status, headers, config) {
			var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
			saveAs(blob, "Order#"+id);
		}).error(function (data, status, headers, config) {
			//upload failed
		});
	};

	// GET orders excel
	this.getOrdersExcel = function (from, to, airport) {
		$http({
			url: 'rest/files/excel/',
			method: "GET",
			params: {from: from, to: to, airport: airport},
			responseType: 'arraybuffer',
			headers: {'token': sessionStorage.getItem('token')}
		}).success(function (data, status, headers, config) {
			var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
			saveAs(blob, "Orders "+from+" "+to);
		}).error(function (data, status, headers, config) {
			//upload failed
		});
	};

	this.getPreview = function(id) {
		return $http.get('rest/orders/preview/'+id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Delete orders/id
	this.deleteOrder = function (order) {
		return $http.delete('rest/orders/' + order.id, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	// Post ration
	this.postRation = function(ration) {
		console.log('ration', ration);
		return $http.post('rest/ration', ration, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	};

	//Post order
	this.postOrder = function (order) {
		return $http.post('rest/orders', order, {
			headers: {'token': sessionStorage.getItem('token')}
		});
	}
}]);