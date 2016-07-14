(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('person', {
            parent: 'space',
            url: '/person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'People'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/person/people.html',
                    controller: 'PersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('person-detail', {
            parent: 'space',
            url: '/person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Person'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/person/person-detail.html',
                    controller: 'PersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Person', function($stateParams, Person) {
                    return Person.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('person.new', {
            parent: 'person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/person/person-dialog.html',
                    controller: 'PersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                name: null,
                                type: null,
                                aka: null,
                                birthday: null,
                                deathday: null,
                                homepage: null,
                                birthPlace: null,
                                imdbId: null,
                                popularity: null,
                                profilePath: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: true });
                }, function() {
                    $state.go('person');
                });
            }]
        })
        .state('person.edit', {
            parent: 'person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/person/person-dialog.html',
                    controller: 'PersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('person.delete', {
            parent: 'person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/person/person-delete-dialog.html',
                    controller: 'PersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Person', function(Person) {
                            return Person.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
