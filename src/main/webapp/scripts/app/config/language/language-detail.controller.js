(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('LanguageDetailController', LanguageDetailController);

    LanguageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Language', 'Movie'];

    function LanguageDetailController($scope, $rootScope, $stateParams, entity, Language, Movie) {
        var vm = this;

        vm.language = entity;

        var unsubscribe = $rootScope.$on('moviespaceApp:languageUpdate', function(event, result) {
            vm.language = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
