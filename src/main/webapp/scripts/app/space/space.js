'use strict';

angular.module('moviespaceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('space', {
                parent: 'site',
                url: '/space',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/space/space.html',
                        controller: 'SpaceController'
                    }
                },
                resolve: {
                    
                }
            });
    });
