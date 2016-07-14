(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .controller('LanguageController', LanguageController);

    LanguageController.$inject = ['$scope', '$state', 'Language'];

    function LanguageController ($scope, $state, Language) {
        var vm = this;
        
        vm.languages = [];

        loadAll();

        function loadAll() {
            Language.query(function(result) {
                vm.languages = result;
            });
        }
    }
})();
