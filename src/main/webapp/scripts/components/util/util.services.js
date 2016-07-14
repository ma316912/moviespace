'use strict';

angular.module('moviespaceApp')
    .factory('UtilService', function ($http,TMDB_IMG_URL) {
        var api = {};

        api.resolveImageUrl = function (imageUrl, size) {
            return TMDB_IMG_URL+"/"+(size?size:'original')+imageUrl+"?api_key=c8a9820e15d305d084c1847470191f3f"
        };

        api.resolveEntityImages = function(entity) {
            if(entity.backdrop_path) 
                entity.backdrop_path = api.resolveImageUrl(entity.backdrop_path);
            if(entity.poster_path) 
                entity.poster_path = api.resolveImageUrl(entity.poster_path);
            if(entity.backdrop_path) 
                entity.profile_path = api.resolveImageUrl(entity.profile_path);
        };

        api.resolveEntityArrayImages = function(arr) {
            if(arr) {
                arr.forEach(function(entity){
                    api.resolveEntityImages(entity);
                });
            }
        };

        api.findInArray = function(array, value, check, fetch){
                /* Finds an element using check as the filed from the array,
                 * optionally pass a fetch param to get a specific filed alone
                 */
                var ret = value;
                array.forEach(function(item){
                    if(item[check] == value){
                        if(fetch){
                            ret = item[fetch];
                        }else{
                            ret = item;
                        }
                        return;
                    }
                });

                return ret;
        };

        return api;
    });
