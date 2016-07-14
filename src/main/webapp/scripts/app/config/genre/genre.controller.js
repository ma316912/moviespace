(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('GenreController', GenreController);

    GenreController.$inject = ['$scope', '$state', 'Genre'];

    function GenreController ($scope, $state, Genre) {
        var vm = this;
        
        vm.genres = [];

        loadAll();

        function loadAll() {
            Genre.query(function(result) {
                vm.genres = result;
            });
        }
    }
})();
