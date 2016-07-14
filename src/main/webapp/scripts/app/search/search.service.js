'use strict';

angular
        .module('moviespaceApp')
        .factory('SearchService', function MovieService($http,DateUtils,TMDB_IMG_URL) {

        var url = "api/search";
        var api = {};

        var retrieveImagesForObject = function(entry) {
                if(entry) {
                    if(entry.backdrop_path)
                        entry.backdrop_path = TMDB_IMG_URL+"/original/"+entry.backdrop_path;
                    if(entry.poster_path)
                        entry.poster_path = TMDB_IMG_URL+"/original/"+entry.poster_path;
                    if(entry.profile_path)
                        entry.profile_path = TMDB_IMG_URL+"/original/"+entry.profile_path;
                }
        };

        var retrieveImagesForList = function(response) {
            response.forEach(function(entry){
                    retrieveImagesForObject(entry);
                });
        };
 
        api.getMoviesByQuery = function(query) {
            return $http.get(url+"/movie?query="+query).success(function(response){
                retrieveImagesForList(response);
            }).error(function(){

            });
        };

        api.getPersonsByQuery = function(query) {
            return $http.get(url+"/person?query="+query).success(function(response){
                retrieveImagesForList(response);
            }).error(function(){

            });
        };        

        api.getCompaniesByQuery = function(query) {
            return $http.get(url+"/company?query="+query).success(function(response){
                retrieveImagesForList(response);
            }).error(function(){

            });
        };

        api.getCollectionsByQuery = function(query) {
            return $http.get(url+"/collection?query="+query).success(function(response){
                retrieveImagesForList(response);
            }).error(function(){

            });
        };

        api.getCollectionInfoById = function(id) {
            return $http.get(url+"/collection/"+id).success(function(response){
                retrieveImagesForObject(response);
               //console.log(response);
                if(response.parts) {
                    retrieveImagesForList(response.parts);
                }
            }).error(function(){

            });
        };        

        return api;
    });