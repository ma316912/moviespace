'use strict';

angular.module('moviespaceApp')
    .directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var language = attrs.activeMenu;

                scope.$watch(function() {
                    return $translate.use();
                }, function(selectedLanguage) {
                    if (language === selectedLanguage) {
                        tmhDynamicLocale.set(language);
                        element.addClass('active');
                    } else {
                        element.removeClass('active');
                    }
                });
            }
        };
    })
    .directive('activeLink', function(location) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var clazz = attrs.activeLink;
                var path = attrs.href;
                path = path.substring(1); //hack because path does bot return including hashbang
                scope.location = location;
                scope.$watch('location.path()', function(newPath) {
                    if (path === newPath) {
                        element.addClass(clazz);
                    } else {
                        element.removeClass(clazz);
                    }
                });
            }
        };
    })
    .directive('widgetDropdown', function($window, $timeout) {
    var bodyEl = angular.element($window.document.body);

    var show = function(parentEl, element) {
        // use timeout to hookup the events to prevent
        // event bubbling from being processed imediately.
        $timeout(function() {
            bodyEl.on('click', function(e) {
                onBodyClick(element, e)
            });
        }, 0, false);
        parentEl.hasClass('dropdown') && parentEl.addClass('open');
    },

        hide = function(parentEl, element) {
            bodyEl.off('click', function(e) {
                onBodyClick(element, e)
            });
            parentEl.hasClass('dropdown') && parentEl.removeClass('open');
        };
    // Private functions
    function onBodyClick(element, evt) {
        if (evt.target === element[0] || $(evt.target).parents('.dropdown-toggle')[0] === element[0]) return;
        return evt.target !== element[0] && $(evt.target).parents('.dropdown-toggle')[0] !== element[0] && hide(element.parent());
    };
    return {
        restrict: "A",
        link: function(scope, element, attrs) {

            element.bind('click', function(e) {
                e.preventDefault();
                var parentEl = element.parent();
                if (parentEl.hasClass('dropdown') && parentEl.hasClass('open')) {
                    hide(parentEl, element);
                } else {
                    show(parentEl, element);
                }

            });


        }
    }
});