(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('CollectionController', CollectionController);

    CollectionController.$inject = ['$scope', '$state', 'Collection'];

    function CollectionController ($scope, $state, Collection) {
        var vm = this;
        
        vm.collections = [];

        loadAll();

        function loadAll() {
            Collection.query(function(result) {
                vm.collections = result;
            });
        }
    }
})();
