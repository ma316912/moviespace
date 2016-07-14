'use strict';

angular.module('moviespaceApp')
     .factory('$exceptionHandler', function($injector) {
        return function(exception, cause) {
            var AlertService = $injector.get('AlertService');
            exception.message += ' (caused by "' + cause + '")';
            //console.log(exception.message);
            console.error(exception);
            //TODO check environment and enable this
            //AlertService.error('Angular Error.\nReason: ' + exception.message);
            //throw exception;
        };
    });