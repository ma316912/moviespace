(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('GenreDetailController', GenreDetailController);

    GenreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Genre', 'Movie','GenreService'];

    function GenreDetailController($scope, $rootScope, $stateParams, entity, Genre, Movie, GenreService) {
        var vm = this;

        vm.genre = entity;
        vm.loading = true;
        GenreService.getMoviesByGenre(vm.genre.id)
                    .success(function(response) {
                        vm.genreMovies = response;
                        vm.loading = false;
                    }).error(function() {
                        vm.loading = true;
                    });

        var unsubscribe = $rootScope.$on('moviespaceApp:genreUpdate', function(event, result) {
            vm.genre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
