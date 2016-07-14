/*(function() {
    'use strict';
    angular
        .module('moviespaceApp')
        .factory('CollectionService', function($resource, DateUtils){

            var url = "api/collections";

            var api = {};

            api.addCollectionToMySpace = function(id) {
                return $http.get(url+"/add/"+id).success(function(response){
                    
                }).error(function(){
                    console.log("Error occured");
                });
            };

        });
})();
*/