(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('CompanyDetailController', CompanyDetailController);

    CompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Company', 'Movie', 'CompanyService'];

    function CompanyDetailController($scope, $rootScope, $stateParams, entity, Company, Movie, CompanyService) {
        var vm = this;

        vm.company = entity;
        vm.movies = [];
        vm.loading = true;
        CompanyService.getMovies(vm.company.id).success(function(response){
            vm.movies = [].concat(response);
            vm.loading = false;
        }).error(function(){
            vm.loading = false;
        });

        var unsubscribe = $rootScope.$on('moviespaceApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
