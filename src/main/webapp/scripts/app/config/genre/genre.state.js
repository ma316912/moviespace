(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('genre', {
            parent: 'config',
            url: '/genre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Genres'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/config/genre/genres.html',
                    controller: 'GenreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('genre-detail', {
            parent: 'config',
            url: '/genre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Genre'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/config/genre/genre-detail.html',
                    controller: 'GenreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Genre', function($stateParams, Genre) {
                    return Genre.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('genre.new', {
            parent: 'genre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/genre/genre-dialog.html',
                    controller: 'GenreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('genre', null, { reload: true });
                }, function() {
                    $state.go('genre');
                });
            }]
        })
        .state('genre.edit', {
            parent: 'genre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/genre/genre-dialog.html',
                    controller: 'GenreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Genre', function(Genre) {
                            return Genre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('genre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('genre.delete', {
            parent: 'genre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/genre/genre-delete-dialog.html',
                    controller: 'GenreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Genre', function(Genre) {
                            return Genre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('genre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
