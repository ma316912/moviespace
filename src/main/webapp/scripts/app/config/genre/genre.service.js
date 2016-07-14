(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('Genre', Genre);

    Genre.$inject = ['$resource'];

    function Genre ($resource) {
        var resourceUrl =  'api/genres/:id';

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
        .factory('GenreService', function($http){

            var url = "api/genres/";
            var api = {};

            api.getMoviesByGenre = function(id) {
                return $http.get(url+id+"/movies").success(function(){
                }).error(function(){
                    console.log("Error finding movies for genre ",id);
                });
            };

            return api;
        });


})();
