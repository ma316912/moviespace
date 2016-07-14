(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('Company', Company);

    Company.$inject = ['$resource'];

    function Company ($resource) {
        var resourceUrl =  'api/companies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }


        angular
        .module('moviespaceApp')
        .factory('CompanyService', function($http){
            var url = "api/companies";

            var api = {};

            api.getMovies = function(id) {
                return $http.get(url+"/"+id+"/movies");
            };

            api.addCompanyToMySpace = function(id) {
                return $http.get(url+"/add/"+id).success(function(){

                }).error(function(){

                });
            };

            api.getTotalCompanyCount = function() {
            return $http.get(url+"/count").success(function(response){
                
            }).error(function(){
                
            });
        };

            return api;
        });    
})();
