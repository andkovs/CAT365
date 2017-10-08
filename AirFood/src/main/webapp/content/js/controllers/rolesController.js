'use strict';

angular.module('myApp').controller('RolesController', ['$scope', 'accountsService', function ($scope, accountsService){

    $scope.title = "Роли";
    $scope.roles = angular.copy($scope.accounts.roles);
    $scope.newRole = {};

    $scope.addRole = function (role) {
        accountsService.postRole(role).then(function (response) {
            var newRole = response.data;
            $scope.roles.push(newRole);
            $scope.newRole = {};
            updateAccounts();
        }, function (response) {
            console.log('add error', response);
        });
    };

    $scope.resetRole = function (role, form) {
        role.rolename = _.findWhere($scope.accounts.roles, { id: role.id }).rolename;
        form.$setPristine();
    };

    $scope.saveRole = function (role, form) {
        accountsService.putRole(role).then(function (response) {
            form.$setPristine();
            updateAccounts();
        }, function (response) {
            console.log('save error', response);
        });
    };

    $scope.removeRole = function (role) {
        if (confirm('Вы уверены, что хотите удалить роль ' + role.rolename + '?')) {
            accountsService.deleteRole(role).then(function (response) {
                $scope.roles = _.without($scope.roles, role);
                updateAccounts();
            }, function (response) {
                console.log('delete error', response);
            });
        }
    };

    function updateAccounts() {
        $scope.accounts.roles = angular.copy($scope.roles);
    };
}]);