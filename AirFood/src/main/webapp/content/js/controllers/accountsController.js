'use strict';

angular.module('myApp').controller('AccountsController', ['accounts', '$state', '$scope', function (accounts, $state, $scope) {
    $scope.title = "Аккаунты";
    $scope.$state = $state;
    $scope.accounts = accounts.data;
    console.log("accounts", $scope.accounts);
}]);
