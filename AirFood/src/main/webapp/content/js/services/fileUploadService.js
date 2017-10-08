'use strict';

angular.module('myApp').service('fileUploadService', ['$http', function ($http) {

    this.uploadFileToUrl = function(file, id, name, type){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('id', id);
        fd.append('name', name);
        fd.append('type', type);
        fd.append('token', sessionStorage.getItem('token'));
        return $http.post('rest/files/upload', fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    };

    this.deleteFile = function(id){
        return $http.delete('rest/files/upload/'+id, {
            headers: {'token': sessionStorage.getItem('token')}
        });
    };

    this.getPDFFile = function(id, orderid, name){
        $http({
            url: 'rest/files/pdf/'+id,
            method: "GET",
            params: {orderid: orderid},
            responseType: 'arraybuffer',
            headers: {'token': sessionStorage.getItem('token')}
        }).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/pdf"});
            saveAs(blob, name);
        }).error(function (data, status, headers, config) {
            //upload failed
        });
    }

}]);
