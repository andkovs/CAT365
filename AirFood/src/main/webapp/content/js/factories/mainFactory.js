'use strict';

angular.module('myApp').factory('mainFactory', function() {
    return {
        data: {
            from: '',
            to: ''
        },
        update: function(from, to) {
            // Improve this method as needed
            console.log('in factory fuuuu', from, to);
            this.data.from = from;
            this.data.to = to;
        }
    };
});
