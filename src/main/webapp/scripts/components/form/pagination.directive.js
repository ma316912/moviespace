/* globals $ */
'use strict';

angular.module('moviespaceApp')
    .directive('moviespaceAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
