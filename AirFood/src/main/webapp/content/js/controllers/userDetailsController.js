angular.module('myApp').controller('UserDetailsController', ['$scope', 'user', 'roles', 'airports', 'userService', '$state',
    function ($scope, user, roles, airports, userService, $state) {
        $scope.title = "Создание/Редактирование пользователя";
        $scope.user = user.data;
        $scope.roles = roles.data;
        $scope.airports = airports.data;
        $scope.tmp = angular.copy($scope.user);
        $scope.changeFlag=false;

        console.log($scope.user);

        $scope.saveUser = function (user, form) {
            if (user.id == 0) {
                user.enabled = true;
                userService.postUser(user).then(function (response) {
                    $state.go('userDetails', {id: response.data.id});
                }, function () {
                });
            }
            else {
                userService.putUser(user).then(function () {
                    form.$setPristine();
                }, function () {
                });
            }
            $scope.tmp = angular.copy($scope.user);
        };

        $scope.resetUser = function (form) {
            $scope.user = angular.copy($scope.tmp);
            $scope.changeFlag=false;
            form.$setPristine();
        };

        $scope.checkAll = function() {
            $scope.user.airports = angular.copy($scope.airports);
            $scope.changeFlag=true;
        };
        $scope.uncheckAll = function() {
            $scope.user.airports = [];
            $scope.changeFlag=true;
        };


    }]);


