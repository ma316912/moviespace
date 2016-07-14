(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('CollectionDetailController', CollectionDetailController);

    CollectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Collection', 'Movie'];

    function CollectionDetailController($scope, $rootScope, $stateParams, entity, Collection, Movie) {
        var vm = this;

        vm.collection = entity;

        var unsubscribe = $rootScope.$on('moviespaceApp:collectionUpdate', function(event, result) {
            vm.collection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
