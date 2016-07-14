 'use strict';

angular.module('moviespaceApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-moviespaceApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-moviespaceApp-params')});
                }
                return response;
            },
            requestError: function(rejectReason) {
                AlertService.error("Server request failed.\nReason " + rejectReason);
                return $q.reject(rejectReason);
            },
            responseError: function(response) {
                var message = "Server request failed."
                var reason = "";
                var errorMess = null;
                var path = (response.data && response.data.path) || response.config.url;
                var skip = false;
                if (response.data && path) {
                     if (!(response.status === 401 && (response.data === '' || (response.data.path && response.data.path.indexOf('/api/account') === 0 )))) {    
                        switch(response.status){
                            case 400:
                                reason = "Validation failed";
                                reason += response.data && response.data.message ? '\n' + response.data.message : '';
                                break;
                            case 403:
                                skip = true;
                                break;
                            case 404:
                                reason = "Resource not found"
                                break;
                            default:
                                reason = "Internal error, please contact administrator."
                                break;
                        }
                        
                        console.error(path + " >> " + (response.data.message ? response.data.message : '')  + ":" + (response.data.description ? response.data.description : ''));
                        if(!skip){
                            AlertService.error(message + '\nReason: '+ reason + (errorMess !== null ? '\nMessage: ' + errorMess: ''));
                            skip = false;
                        }
                    }
                } else if(response.status == 0){
                    AlertService.error('Server not reachable, please contact administrator.');
                }else {
                    var alertKey = response.headers('X-moviespaceApp-error');
                        if (angular.isString(alertKey)) {
                            AlertService.error(alertKey, { param : response.headers('X-moviespaceApp-params')});
                        }
                }
                return $q.reject(response);
            }
        };
    });
