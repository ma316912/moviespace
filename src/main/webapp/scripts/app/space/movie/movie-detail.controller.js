(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$filter', 'entity', 'Movie', 'Genre', 'Company', 'Country', 'Language', 'Review', 'Credits', 'Collection'];

    function MovieDetailController($scope, $rootScope, $stateParams, $filter, entity, Movie, Genre, Company, Country, Language, Review, Credits, Collection) {
        var vm = this;

        vm.movie = entity;

        if(vm.movie && vm.movie.credit && vm.movie.credit.cast) {
            vm.movie.credit.cast = $filter('orderBy')(vm.movie.credit.cast, 'order');
        }

        var unsubscribe = $rootScope.$on('moviespaceApp:movieUpdate', function(event, result) {
            vm.movie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
