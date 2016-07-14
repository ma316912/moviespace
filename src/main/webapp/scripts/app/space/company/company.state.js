(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company', {
            parent: 'space',
            url: '/company',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Companies'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/company/companies.html',
                    controller: 'CompanyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('company-detail', {
            parent: 'space',
            url: '/company/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Company'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/company/company-detail.html',
                    controller: 'CompanyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Company', function($stateParams, Company) {
                    return Company.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('company.new', {
            parent: 'company',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                name: null,
                                description: null,
                                headquarters: null,
                                homepage: null,
                                logoPath: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: true });
                }, function() {
                    $state.go('company');
                });
            }]
        })
        .state('company.edit', {
            parent: 'company',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/company/company-dialog.html',
                    controller: 'CompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company.delete', {
            parent: 'company',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/company/company-delete-dialog.html',
                    controller: 'CompanyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Company', function(Company) {
                            return Company.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
