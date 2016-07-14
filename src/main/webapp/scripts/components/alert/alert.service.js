/*(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .provider('AlertService', AlertService);

    function AlertService () {
        this.toast = false;
        this.$get = getService;

        this.showAsToast = function(isToast) {
            this.toast = isToast;
        };

        getService.$inject = ['$timeout', '$sce'];

        function getService ($timeout, $sce) {
            var toast = this.toast,
                alertId = 0, // unique id for each alert. Starts from 0.
                alerts = [],
                timeout = 5000; // default timeout

            return {
                factory: factory,
                isToast: isToast,
                add: addAlert,
                closeAlert: closeAlert,
                closeAlertByIndex: closeAlertByIndex,
                clear: clear,
                get: get,
                success: success,
                error: error,
                info: info,
                warning : warning
            };

            function isToast() {
                return toast;
            }

            function clear() {
                alerts = [];
            }

            function get() {
                return alerts;
            }

            function success(msg, params, position) {
                return this.add({
                    type: 'success',
                    msg: msg,
                    params: params,
                    timeout: timeout,
                    toast: toast,
                    position: position
                });
            }

            function error(msg, params, position) {
                return this.add({
                    type: 'danger',
                    msg: msg,
                    params: params,
                    timeout: timeout,
                    toast: toast,
                    position: position
                });
            }

            function warning(msg, params, position) {
                return this.add({
                    type: 'warning',
                    msg: msg,
                    params: params,
                    timeout: timeout,
                    toast: toast,
                    position: position
                });
            }

            function info(msg, params, position) {
                return this.add({
                    type: 'info',
                    msg: msg,
                    params: params,
                    timeout: timeout,
                    toast: toast,
                    position: position
                });
            }

            function factory(alertOptions) {
                var alert = {
                    type: alertOptions.type,
                    msg: $sce.trustAsHtml(alertOptions.msg),
                    id: alertOptions.alertId,
                    timeout: alertOptions.timeout,
                    toast: alertOptions.toast,
                    position: alertOptions.position ? alertOptions.position : 'top right',
                    scoped: alertOptions.scoped,
                    close: function (alerts) {
                        return closeAlert(this.id, alerts);
                    }
                };
                if(!alert.scoped) {
                    alerts.push(alert);
                }
                return alert;
            }

            function addAlert(alertOptions, extAlerts) {
                alertOptions.alertId = alertId++;
                var that = this;
                var alert = this.factory(alertOptions);
                if (alertOptions.timeout && alertOptions.timeout > 0) {
                    $timeout(function () {
                        that.closeAlert(alertOptions.alertId, extAlerts);
                    }, alertOptions.timeout);
                }
                return alert;
            }

            function closeAlert(id, extAlerts) {
                var thisAlerts = extAlerts ? extAlerts : alerts;
                return closeAlertByIndex(thisAlerts.map(function(e) { return e.id; }).indexOf(id), thisAlerts);
            }

            function closeAlertByIndex(index, thisAlerts) {
                return thisAlerts.splice(index, 1);
            }
        }
    }
})();
*/

'use strict';

angular.module('moviespaceApp')
    .factory('AlertService', function ($timeout, $sce) {
        var exports = {
            factory: factory,
            add: addAlert,
            closeAlert: closeAlert,
            closeAlertByIndex: closeAlertByIndex,
            clear: clear,
            get: get,
            success: success,
            error: error,
            info: info,
            warning : warning
        },
        alertId = 0, // unique id for each alert. Starts from 0.
        alerts = [],
        timeout = 4000;

        function clear() {
            alerts = [];
        }

        function get() {
            return alerts;
        }

        function success(msg) {
            this.add({
                type: "success",
                msg: msg,
                timeout: timeout
            });
        }

        function error(msg) {
            this.add({
                type: "danger",
                msg: msg,
                timeout: timeout
            });
        }

        function warning(msg) {
            this.add({
                type: "warning",
                msg: msg,
                timeout: timeout
            });
        }

        function info(msg) {
            this.add({
                type: "info",
                msg: msg,
                timeout: timeout
            });
        }

        function factory(alertOptions) {
            return alerts.push({
                type: alertOptions.type,
                msg: $sce.trustAsHtml(alertOptions.msg),
                message: alertOptions.msg, // to view the alerts on the console.
                id: alertOptions.alertId,
                timeout: alertOptions.timeout,
                close: function () {
                    return exports.closeAlert(this.id);
                }
            });
        }

        function addAlert(alertOptions) {
            alertOptions.alertId = alertId++;
            alertOptions.message = alertOptions.msg;
            var that = this;
            this.factory(alertOptions);
            if (alertOptions.timeout && alertOptions.timeout > 0) {
                $timeout(function () {
                    that.closeAlert(alertOptions.alertId);
                }, alertOptions.timeout);
            }
        }

        function closeAlert(id) {
            return this.closeAlertByIndex(alerts.map(function(e) { return e.id; }).indexOf(id));
        }

        function closeAlertByIndex(index) {
            return alerts.splice(index, 1);
        }



        return exports;

    });
