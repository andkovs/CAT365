'use strict';

angular.module('myApp').controller('UsersController', ['users', '$scope', 'userService', function (users, $scope, userService) {
    $scope.title = "Пользователи";
    $scope.users = users.data;

    $scope.search   = '';

    $scope.removeUser = function (user) {
        if (confirm('Вы уверены, что хотите удалить пользователя ' + user.login + '?')) {
            userService.deleteUser(user).then(function (response) {
                $scope.users = _.without($scope.users, user);
            }, function (response) {
            });
        }
    };

    $scope.enableUser = function(user){
        userService.enableUser(user.id).then(function (response) {
            user.enabled = true;
        }, function (response) {
        });
        // form.$setPristine();
    };

    $scope.disableUser = function(user){
        userService.disableUser(user.id).then(function (response) {
            user.enabled = false;
        }, function (response) {
        });
        // form.$setPristine();
    };


    // $scope.preview = function(id) {
    //     var getUser = userService.getUser(id);
    //     getUser.then(function(response){
    //         $scope.previewData = response.data;
    //         $('#userPreview').modal();
    //     });
    // };
    //
    // $('#userPreview').on('hidden.bs.modal', function () {
    //     $scope.previewData = {};
    // })

}]);

// 'use strict';
//
// angular.module('myApp').controller('UsersController', ['$scope', 'accountsService', function ($scope, accountsService){
//
//     $scope.title = "Пользователи";
//     $scope.users = angular.copy($scope.accounts.users);
//     $scope.roles = getRoleNames();
//     console.log('roles', $scope.roles);
//     $scope.newUser = {};
//
//     function getRoleNames(){
//         var list = [];
//         angular.forEach($scope.accounts.roles, function(role) {
//             list.push(role.rolename);
//         });
//         return list;
//     };
//
//     $scope.addUser = function (user) {
//         console.log('new usre', user);
//         accountsService.postUser(user).then(function (response) {
//             console.log('add success', response);
//             var newUser = response.data;
//             $scope.users.push(newUser);
//             $scope.newUser = {};
//             updateAccounts();
//         }, function (response) {
//             console.log('add error', response);
//         });
//     };
//
//     $scope.resetUser = function (user, form) {
//         user.login = _.findWhere($scope.accounts.users, { id: user.id }).login;
//         user.password = _.findWhere($scope.accounts.users, { id: user.id }).password;
//         user.firstName = _.findWhere($scope.accounts.users, { id: user.id }).firstName;
//         user.lastName = _.findWhere($scope.accounts.users, { id: user.id }).lastName;
//         user.email = _.findWhere($scope.accounts.users, { id: user.id }).email;
//         user.roles = _.findWhere($scope.accounts.users, { id: user.id }).roles;
//         form.$setPristine();
//     };
//
//     $scope.saveUser = function (user, form) {
//         accountsService.putUser(user).then(function (response) {
//             form.$setPristine();
//             updateAccounts();
//         }, function (response) {
//             console.log('save error', response);
//         });
//     };
//
//     $scope.removeUser = function (user) {
//         if (confirm('Вы уверены, что хотите удалить пользователя ' + user.login + '?')) {
//             accountsService.deleteUser(user).then(function (response) {
//                 $scope.users = _.without($scope.users, user);
//                 updateAccounts();
//             }, function (response) {
//                 console.log('delete error', response);
//             });
//         }
//     };
//
//     function updateAccounts() {
//         $scope.accounts.users = angular.copy($scope.users);
//     };
// }]);