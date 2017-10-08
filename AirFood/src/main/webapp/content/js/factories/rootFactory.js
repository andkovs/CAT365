// 'use strict';
//
// angular.module('myApp').factory('rootFactory', function() {
//     return {
//         data: {
//             name: '',
//             role: []
//         },
//         update: function(name, role) {
//             this.data.name = name;
//             this.data.role = role;
//         },
//         getName: function () {
//             return this.data.name;
//         },
//         getRole: function () {
//             return this.data.role;
//         }
//     };
// });

'use strict';

angular.module('myApp').factory('rootFactory', function() {
    return {
        data: {
            user: {},
            roles: []
        },
        update: function(user, roles) {
            this.data.user = angular.copy(user);
            this.data.roles = angular.copy(roles);
        },
        getUser: function () {
            return this.data.user;
        },
        getRoles: function () {
            return this.data.roles;
        }
    };
});
