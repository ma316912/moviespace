(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('Person', Person);

    Person.$inject = ['$resource', 'DateUtils'];

    function Person ($resource, DateUtils) {
        var resourceUrl =  'api/people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthday = DateUtils.convertDateTimeFromServer(data.birthday);
                        data.deathday = DateUtils.convertDateTimeFromServer(data.deathday);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }


    angular
        .module('moviespaceApp')
        .factory('PersonService', function($http){
            var url = "api/people";
            var api = {};

            api.getMovies = function(id) {
                return $http.get(url+"/"+id+"/movies");
            };

            api.addPersonToMySpace = function(id) {
                return $http.get(url+"/add/"+id).success(function(response){
                
                }).error(function(){
                
                });
            };

            api.getTotalPeopleCount = function() {
            return $http.get(url+"/count").success(function(response){
                
            }).error(function(){
                
            });
        };

            return api;
        });


})();
