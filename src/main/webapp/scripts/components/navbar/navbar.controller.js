'use strict';

angular.module('moviespaceApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        
        $scope.$state = $state;

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('login');
        };

        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        /*ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });*/

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }


        $scope.toggleMenu = function () {
            //Enable sidebar toggle
            //Enable sidebar push menu
            $("body").toggleClass('sidebar-collapse');
            $("body").toggleClass('sidebar-open');

        };

        $scope.setFont = function (size) {
            if(!size || size === null || size.length === 0){
                size = 's';
            }
            var add, remove;
            switch(size){
                case 's':
                    add = 'font-small';
                    remove = 'font-medium font-large';
                    break;
                case 'm':
                    add = 'font-medium';
                    remove = 'font-small font-large';
                    break;
                case 'l':
                    add = 'font-large';
                    remove = 'font-medium font-small';
                    break;
            }

            $('body').removeClass(remove);
            $('body').addClass(add);
            $timeout(function(){
                $('.font-btn').removeClass('font-active');
                $('.font-' + size).addClass('font-active');
            }, 100)
            //StorageService.save($scope.account.login + ".user.font.pref", size);
        };

    });
