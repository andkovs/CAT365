angular.module('myApp').service('sessionService', ['$http', function ($http) {

    // Get session
    this.getSession = function () {
        return $http.get('rest/session', {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Post login
    this.postLogin = function (login) {
        return $http.post('rest/session/login', login);
    };

}]);