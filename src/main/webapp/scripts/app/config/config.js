'use strict';

angular.module('moviespaceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('config', {
                abstract: true,
                parent: 'site'
            });
    });
