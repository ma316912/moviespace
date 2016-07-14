'use strict';

angular.module('moviespaceApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


