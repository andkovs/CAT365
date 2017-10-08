'use strict';

angular.module('myApp').controller('HomeController', ['$scope', '$state', 'rootFactory', 'sessionService',
    function ($scope, $state, rootFactory, sessionService) {

        $scope.login = {};
        $scope.invalidLoginOrPass = false;
        $scope.invalidText = "";

        $scope.updateOrdersTime = function (from, to) {
            sessionStorage.setItem('from', from);
            sessionStorage.setItem('to', to);
            console.log('session storage', sessionStorage.getItem('from'), sessionStorage.getItem('to'));
        };

        $scope.getOrders = function () {
            var to = moment().add(1, 'days').hours(12).minutes(0).format('DD-MM-YYYY HH:mm');
            var from = moment().subtract(1, 'days').hours(12).minutes(0).format('DD-MM-YYYY HH:mm');
            $scope.updateOrdersTime(from, to);
            console.log('session storage', sessionStorage.getItem('from'), sessionStorage.getItem('to'));
            $state.go('orders');
        };

        $scope.authorization = function () {
            var login = angular.copy($scope.login);
            $scope.login.login = "";
            $scope.login.password = "";
            sessionService.postLogin(login).then(function (response) {
                if (response.status === 402) {
                    $scope.invalidLoginOrPass = true;
                    $scope.invalidText = response.data.message;
                } else {
                    rootFactory.update(response.data.user, response.data.roles);
                    sessionStorage.setItem('token', response.data.token);
                }
            }).catch(function (response) {
            });
        }
    }]);