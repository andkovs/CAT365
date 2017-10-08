'use strict';

// App

angular.module('myApp', ['ui.router', 'checklist-model', 'ngCookies', 'file-model']).run(function($rootScope) {
    $rootScope.isLoading = true;
});

// Routing

angular.module('myApp').config([
    '$stateProvider',
    '$urlRouterProvider',
    '$locationProvider',
    function ($stateProvider, $urlRouterProvider, $locationProvider) {
        $stateProvider.state('home', {
            url: '/',
            controller: 'HomeController',
            templateUrl: './partials/home.html'
            // resolve: {
            //     session: ['sessionService', function (sessionService) {
            //         return sessionService.get();
            //     }]
            // }
        })
        //     .state('accounts', {
        //     url: '/accounts',
        //     controller: 'AccountsController',
        //     resolve: {
        //         accounts: ['accountsService', function (accountsService) {
        //             return accountsService.get();
        //         }]
        //     },
        //     templateUrl: './partials/accounts.html'
        // }).state('accounts.users', {
        //     url: '/users',
        //     controller: 'UsersController',
        //     templateUrl: './partials/users.html'
        // }).state('accounts.roles', {
        //     url: '/roles',
        //     controller: 'RolesController',
        //     templateUrl: './partials/roles.html'
        // })
            .state('users', {
                url: '/users',
                controller: 'UsersController',
                resolve: {
                    users: ['userService', function (userService) {
                        return userService.get();
                    }]
                },
                templateUrl: './partials/users.html'
            }).state('userDetails', {
            url: '/user/details/:id',
            controller: 'UserDetailsController',
            resolve: {
                user: ['userService', '$stateParams', function (userService, $stateParams) {
                    return userService.getUser($stateParams.id);
                }],
                roles: ['roleService', function (roleService) {
                    return roleService.get();
                }],
                airports: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.getAirports();
                }]
            },
            templateUrl: './partials/userDetails.html'
        }).state('orders', {
            url: '/orders',
            controller: 'OrdersController',
            resolve: {
                dictionary: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.get();
                }],
                airports: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.getAirportsByUserId();
                }],
                orders: ['ordersService', function (ordersService) {
                    console.log('in app.js', sessionStorage.getItem('from'), sessionStorage.getItem('to'));
                    return ordersService.get(sessionStorage.getItem('from'), sessionStorage.getItem('to'));
                }]
            },
            templateUrl: './partials/orders.html'
        }).state('ordersDetails', {
            url: '/orders/details/:id',
            controller: 'OrderDetailsController',
            templateUrl: './partials/orderDetails.html',
            resolve: {
                dictionary: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.get();
                }],
                airports: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.getAirportsByUserId();
                }],
                order: ['$http','$stateParams', 'ordersService', function($http, $stateParams, ordersService) {
                    return ordersService.getDetails($stateParams.id);
                }]
            }
        }).state('dictionary', {
            url: '/dictionary',
            controller: 'DictionaryController',
            resolve: {
                dictionary: ['dictionaryService', function (dictionaryService) {
                    return dictionaryService.get();
                }]
            },
            templateUrl: './partials/dictionary.html'
        }).state('dictionary.airports', {
            url: '/airports',
            controller: 'AirportsController',
            templateUrl: './partials/airports.html'
        }).state('dictionary.airlines', {
            url: '/airlines',
            controller: 'AirlinesController',
            templateUrl: './partials/airlines.html'
        }).state('dictionary.boards', {
            url: '/boards',
            controller: 'BoardsController',
            templateUrl: './partials/boards.html'
        }).state('dictionary.flights', {
            url: '/flights',
            controller: 'FlightsController',
            templateUrl: './partials/flights.html'
        });
        $urlRouterProvider.otherwise('/');
        // $locationProvider.html5Mode({
        //   enabled: true,
        //   requireBase: false
        // });
    }
]);

// Loading bar

// angular.module('myApp').config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
//     cfpLoadingBarProvider.latencyThreshold = 0;
// }]);

// Http interceptor

angular.module('myApp')
    .factory('myHttpInterceptor', ['$q', '$rootScope', 'rootFactory', '$injector',
        function($q, $rootScope, rootFactory, $injector) {
        return {
            request: function(config) {
                $rootScope.isLoading = true;
                return config;
            },
            requestError: function(config) {
                $rootScope.isLoading = false;
                return config;
            },
            response: function(res) {
                console.log('response succes', res);
                $rootScope.isLoading = false;
                return res;
            },
            responseError: function(res) {
                console.log('response error', res);
                $rootScope.isLoading = false;
                if (res.status === 400) {
                    alert(res.data.message);
                    return $q.reject(res.data.message);
                }
                if(res.status === 401) {
                    rootFactory.update(null, null);
                    sessionStorage.setItem('token', "");
                    $injector.get('$state').transitionTo('home');
                    return $q.reject(res);
                }
                return res;
            }
        }
    }])
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('myHttpInterceptor');
    });










