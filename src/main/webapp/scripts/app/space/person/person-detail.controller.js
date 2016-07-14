(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person','PersonService'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person,PersonService) {
        var vm = this;

        vm.person = entity;
        vm.movies = [];
        vm.loading = true;
        PersonService.getMovies(vm.person.id).success(function(response){
            vm.movies = [].concat(response);
            vm.loading = false;
        }).error(function(){
            vm.loading = false;
        });

        var unsubscribe = $rootScope.$on('moviespaceApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
