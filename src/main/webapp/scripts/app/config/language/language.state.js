(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('language', {
            parent: 'config',
            url: '/language',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Languages'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/config/language/languages.html',
                    controller: 'LanguageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('language-detail', {
            parent: 'config',
            url: '/language/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Language'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/config/language/language-detail.html',
                    controller: 'LanguageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Language', function($stateParams, Language) {
                    return Language.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('language.new', {
            parent: 'language',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/language/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                isoCode: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: true });
                }, function() {
                    $state.go('language');
                });
            }]
        })
        .state('language.edit', {
            parent: 'language',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/language/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('language.delete', {
            parent: 'language',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/config/language/language-delete-dialog.html',
                    controller: 'LanguageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('language', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
