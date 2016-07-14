'use strict';

angular.module('moviespaceApp')
    .controller('WrapperController', function ($scope,AlertService) {

        $scope.hideMenu = function () {

            //Enable hide menu when clicking on the content-wrapper on small screens
            if ($(window).width() <= 1024 && $("body").hasClass("sidebar-open")) {
                $("body").removeClass('sidebar-open');
                $("body").addClass('sidebar-collapse');
            }

        };
        $scope.alerts = AlertService.get();
        /*$scope.hidePanels = function (event) {

            //hide all open panels in navbar when clicked outside navbar-nav dropdown-menu
            if (!$(event.target).parents().hasClass('navbar-custom-menu')) {
                $scope.navbar.activePanels = [];
            }


        };
        $scope.navbar = {
            activePanels: []
        };*/

    });