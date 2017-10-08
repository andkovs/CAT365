angular.module('myApp').service('accountsService', ['$http', function ($http) {

    // Get accounts
    this.get = function () {
        return $http.get('rest/accounts', {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Post user
    this.postUser = function (user) {
        console.log('in service', user);
        return $http.post('rest/users', user, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Put user
    this.putUser = function (user) {
        return $http.put('rest/users/' + user.id, user, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    // Delete user
    this.deleteUser = function (user) {
        return $http.delete('rest/users/' + user.id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };
}]);
