(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('Movie', Movie);

    Movie.$inject = ['$resource', 'DateUtils'];

    function Movie ($resource, DateUtils) {
        var resourceUrl =  'api/movies/:id';

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


angular
        .module('moviespaceApp')
        .factory('MovieService', function($http,UtilService){
            var url = "api/movies",movieUrl="api/movie";
            var api ={};      

        api.getPopularMovies = function(){
            return $http.get(url+"/popular").success(function(response){
                if(response)
                    UtilService.resolveEntityArrayImages(response);
            }).error(function(){

            });
        };

        api.getTopRatedMovies = function(){
            return $http.get(url+"/top-rated").success(function(response){
                if(response)
                    UtilService.resolveEntityArrayImages(response);
            }).error(function(){
                
            });
        };

        api.getLatestMovies = function(){
            return $http.get(url+"/latest").success(function(response){
                if(response)
                    UtilService.resolveEntityArrayImages(response);
            }).error(function(){
                
            });
        };

        api.getUpcomingMovies = function(){
            return $http.get(url+"/upcoming").success(function(response){
                if(response)
                    UtilService.resolveEntityArrayImages(response);
            }).error(function(){
                
            });
        };

        api.saveToMySpace = function(id){
            return $http.get(movieUrl+"/add/"+id).success(function(response){
            }).error(function(){
                
            });
        };

        api.getTotalMovieCount = function() {
            return $http.get(url+"/count").success(function(response){
                
            }).error(function(){
                
            });
        };
       
     return api;  
        });


})();
