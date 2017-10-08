'use strict';

angular.module('myApp').service('userService', ['$http', function ($http) {

    // Get users
    this.get = function () {
        return $http.get('rest/users', {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Get user
    this.getUser = function (id) {
        return $http.get('rest/users/'+id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Put user
    this.putUser = function (user) {
        return $http.put('rest/users/' + user.id, user, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Post user
    this.postUser = function (user) {
        return $http.post('rest/users', user, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Delete user
    this.deleteUser = function (user) {
        return $http.delete('rest/users/' + user.id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Enable user
    this.enableUser = function (id){
        return $http.delete('rest/users/enable/' + id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    }

    // Disable user
    this.disableUser = function (id){
        return $http.delete('rest/users/disable/' + id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    }

}]);

