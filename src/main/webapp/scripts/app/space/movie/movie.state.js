(function() {
    'use strict';

    angular
        .module('moviespaceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('movie', {
            parent: 'space',
            url: '/movie',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Movies'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/movie/movies.html',
                    controller: 'MovieController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('movie-detail', {
            parent: 'space',
            url: '/movie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Movie'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/space/movie/movie-detail.html',
                    controller: 'MovieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Movie', function($stateParams, Movie) {
                    return Movie.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('movie.new', {
            parent: 'movie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/movie/movie-dialog.html',
                    controller: 'MovieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalId: null,
                                title: null,
                                originalTitle: null,
                                popularity: null,
                                backdropPath: null,
                                posterPath: null,
                                releaseDate: null,
                                adult: null,
                                budget: null,
                                homePage: null,
                                overview: null,
                                imdbId: null,
                                revenue: null,
                                runtime: null,
                                tagline: null,
                                userRating: null,
                                voteAverage: null,
                                voteCount: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: true });
                }, function() {
                    $state.go('movie');
                });
            }]
        })
        .state('movie.edit', {
            parent: 'movie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/movie/movie-dialog.html',
                    controller: 'MovieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Movie', function(Movie) {
                            return Movie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movie.delete', {
            parent: 'movie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/space/movie/movie-delete-dialog.html',
                    controller: 'MovieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Movie', function(Movie) {
                            return Movie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('movie', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
