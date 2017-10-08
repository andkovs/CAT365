'use strict';

angular.module('myApp').controller('RootController', ['$scope', '$state', 'rootFactory', 'sessionService',
    function ($scope, $state, rootFactory, sessionService) {

        $scope.rootFactory = rootFactory.data;

        $scope.$watch(function () {
            return rootFactory.getUser();
        }, function (newValue, oldValue) {
            if (newValue !== oldValue) $scope.user = newValue;
        });

        $scope.$watch(function () {
            return rootFactory.getRoles();
        }, function (newValue, oldValue) {
            if (newValue !== oldValue) $scope.roles = newValue;
        });

        function getSession() {
            sessionService.getSession().then(function (response) {
                if (response.status === 200) {
                    rootFactory.update(response.data.user, response.data.roles);
                }
            }, function () {
            });
        }

        getSession();

        $scope.logout = function () {
            rootFactory.update(null, null);
            sessionStorage.setItem('token', "");
            $state.go("home");
        };

    }]);