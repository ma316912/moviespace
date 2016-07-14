(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('Collection', Collection);

    Collection.$inject = ['$resource', 'DateUtils'];

    function Collection ($resource, DateUtils) {
        var resourceUrl =  'api/collections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.releaseDate = DateUtils.convertDateTimeFromServer(data.releaseDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

   angular.module('moviespaceApp')
        .factory('CollectionService', function($http, DateUtils){

            var url = "api/collections";

            var api = {};

            api.addCollectionToMySpace = function(id) {
                return $http.get(url+"/add/"+id).success(function(response){
                    
                }).error(function(){
                    console.log("Error occured");
                });
            };

            api.getTotalCollectionCount = function() {
                return $http.get(url+"/count").success(function(){

                }).error(function(){
                    
                });
            };

            return api;
        });

})();
