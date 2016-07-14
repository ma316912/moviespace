(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('collection', {
            parent: 'space',
            url: '/collection',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collections'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/collection/collections.html',
                    controller: 'CollectionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('collection-detail', {
            parent: 'space',
            url: '/collection/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Collection'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/collection/collection-detail.html',
                    controller: 'CollectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Collection', function($stateParams, Collection) {
                    return Collection.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('collection.new', {
            parent: 'collection',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/collection/collection-dialog.html',
                    controller: 'CollectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                name: null,
                                title: null,
                                overview: null,
                                posterPath: null,
                                backdropPath: null,
                                releaseDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: true });
                }, function() {
                    $state.go('collection');
                });
            }]
        })
        .state('collection.edit', {
            parent: 'collection',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/collection/collection-dialog.html',
                    controller: 'CollectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Collection', function(Collection) {
                            return Collection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('collection.delete', {
            parent: 'collection',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/collection/collection-delete-dialog.html',
                    controller: 'CollectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Collection', function(Collection) {
                            return Collection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('collection', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
