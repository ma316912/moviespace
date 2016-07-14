(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('PersonController', PersonController);

    PersonController.$inject = ['$scope', '$state', 'Person'];

    function PersonController ($scope, $state, Person) {
        var vm = this;
        
        vm.people = [];
        vm.loading = true;
        loadAll();

        function loadAll() {
            Person.query(function(result) {
                vm.people = result;
                vm.loading = false;
            },function(){
                vm.loading = false;
            });
        }

        $scope.getPersonTypeLabel = function(type) {
            if(type=='cast')
                return "label-success";
            else if(type=='crew')
                return "label-warning";
            else
                return "label-primary";
        };

    }
})();
