(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$scope', '$state', 'Company'];

    function CompanyController ($scope, $state, Company) {
        var vm = this;
        
        vm.companies = [];
        vm.loading = true;
        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
                vm.loading = false;
            },function(){
                vm.loading = false;
            });
        }
    }
})();
