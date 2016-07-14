// camelCase To Human/dashed Filter
// ---------------------
// Converts a camelCase string to a human readable string.
// i.e. myVariableName => My Variable Name
// i.e. myVariableName => my-variable-name
// i.e. my-variable-name => myVariableName
// i.e. my-variable-name => My Variable Name
angular.module('moviespaceApp')
    .filter('escape', function () {
        return window.encodeURIComponent;
    })
    .filter('timeAgo', ['nowTime', 'timeAgo', function (nowTime, timeAgo) {
        return function (value) {
            var fromTime = timeAgo.parse(value);
            var diff = nowTime() - fromTime;
            return timeAgo.inWords(diff);
        };
    }])
    .filter('minutesToHours', function () {
        return function (value) {
            var hours = Math.floor(Math.abs(value)/60);
            var minutes = Math.abs(value)%60;
            var duration = "";
            duration = hours ? hours + " Hours ":"";
            duration += minutes ? minutes+" Minutes":"";
            return duration;
        };
    });
