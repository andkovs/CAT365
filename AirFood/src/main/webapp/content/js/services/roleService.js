'use strict';

angular.module('myApp').service('roleService', ['$http', function ($http) {

    // Get roles
    this.get = function () {
        return $http.get('rest/roles', {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };  

}]);