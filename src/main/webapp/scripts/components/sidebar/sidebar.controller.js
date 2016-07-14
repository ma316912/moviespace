'use strict';

angular.module('moviespaceApp')
    .controller('SidebarController', function($scope, $location, $state, $filter, Auth, Principal) {
        
        $scope.account = null;
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            loadSidebar();
        });

        $scope.menuFilterText = '';
        $scope.menuItems = [{
            header: "MENU",
            items: []
        }];

        $scope.tempMenus = [];

        $scope.$on('user-identity-resolved', function(event, args) {
            $scope.account = args;
        });

        $scope.$watch('account', function(newValue, oldValue) {
            if(newValue != oldValue && newValue){
                loadSidebar();
            }
        });

        function loadSidebar(){
            if($scope.isAuthenticated && $scope.isAuthenticated()){
                $scope.tempMenus = [];
                setMainMenu();
            }
        }

        function setMainMenu(){
                    $scope.tempMenus.push(
                        {
                        dispLabel : 'Dashboard',
                            href : 'space',
                            icon : 'fa-dashboard',
                            menuHasSubModules : false
                        },{
                        dispLabel : 'Search',
                            href : 'search',
                            icon : 'fa-search',
                            id: 1,
                            menuHasSubModules : false
                        },
                        {
                            dispLabel : 'MySpace',
                            href : 'space',
                            icon : 'fa-bolt',
                            id: 1,
                            menuHasSubModules : true,
                            subModules:[{
                                dispLabel : 'Movies',
                                href : 'movie',
                                icon : 'fa-film',
                                menuHasSections : false
                            },
                            {
                                dispLabel : 'Collections',
                                href : 'collection',
                                icon : 'fa-slack',
                                menuHasSections : false
                            },
                            {
                                dispLabel : 'People',
                                href : 'person',
                                icon : 'fa-users',
                                menuHasSections : false
                            },
                            {
                                dispLabel : 'Companies',
                                href : 'company',
                                icon : 'fa-building',
                                menuHasSections : false
                            }
                            ]
                        },
                        {
                            dispLabel : 'Config',
                            href : '#',
                            icon : 'fa-cog',
                            id: 1,
                            menuHasSubModules : true,
                            subModules:[{
                                dispLabel : 'Genres',
                                href : 'genre',
                                icon : 'fa-star',
                                menuHasSections : false
                            },
                            {
                                dispLabel : 'Languages',
                                href : 'language',
                                icon : 'fa-language',
                                menuHasSections : false
                            }]
                        });    

                    $scope.menuItems[0].items = $scope.tempMenus;
        }
    });
