'use strict';

angular.module('moviespaceApp')
    .controller('MainController', function ($scope,$state, Principal,$uibModal, MovieService,UtilService) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.movies = {popularMovies:[],upcomingMovies:[],latestMovies:[],topRatedMovies:[]};

        $scope.slides = [];
        $scope.indexes = [];
        var prepareSlides = function(movies) {
            var index = 0;
            $scope.slides = [];
            movies.forEach(function(movie){
                    $scope.slides.push({
                        id:index,
                        title:movie.title,
                        poster: movie.poster_path!=null?movie.poster_path:movie.backdrop_path,
                        overview:movie.overview,
                        link:movie
                    });
                    $scope.indexes.push(index);
                    index++;
            });
            console.log("Total Slides ", $scope.slides.length);
        };

        $scope.getPopularMovies = function() {
            MovieService.getPopularMovies().success(function(response){
            $scope.movies.popularMovies = response;
            console.log(response);
            if($scope.movies.popularMovies) {
                prepareSlides($scope.movies.popularMovies);
            }
        }).error(function(response){

        });
        };

        $scope.getNowPlayingMovies = function() {
            MovieService.getLatestMovies().success(function(response){
                $scope.movies.latestMovies = response;
            console.log(response);
            if($scope.movies.latestMovies) {
                prepareSlides($scope.movies.latestMovies);
            }
            }).error(function(){

            });
        };

        $scope.getUpcomingMovies = function() {
            MovieService.getUpcomingMovies().success(function(response){
            $scope.movies.upcomingMovies = response;
            console.log(response);
            if($scope.movies.upcomingMovies) {
                prepareSlides($scope.movies.upcomingMovies);
            }
        }).error(function(response){

        });
        };

        $scope.getTopRatedMovies = function() {
            MovieService.getTopRatedMovies().success(function(response){
            $scope.movies.topRatedMovies = response;
            console.log(response);
            if($scope.movies.topRatedMovies) {
                prepareSlides($scope.movies.topRatedMovies);
            }
        }).error(function(response){

        });
        };



        


        $scope.showMovieDetail = function(movie) {
            $uibModal.open({
                    templateUrl: 'scripts/app/main/movies/movie-detail-dialog.html',
                    controller: 'MovieDetailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        movie: function () {
                            return movie;
                        }
                    }
                }).result.then(function() {
                    //$state.go('^', null, { reload: true });
                }, function() {
                    //$state.go('^');
                });
        }

            $scope.getPopularMovies();
            $scope.getNowPlayingMovies();
            $scope.getUpcomingMovies();
            $scope.getTopRatedMovies();
    })
    .controller('MovieDetailDialogController', function($timeout, $scope, $stateParams, $uibModalInstance, movie, MovieService, AlertService){
        var vm = this;
        $scope.movie = movie;
        console.log("In movie detail modal");    

        $scope.getImdbMovie = function() {
            MovieService.getMovieFromImdb($scope.movie.id);
        }

        $scope.saveToMySpace = function() {
            MovieService.saveToMySpace($scope.movie.id).success(function(){
                vm.close();     
            }).error(function(){
                //AlertService.error("Error Adding movie to MySpace..");
            }); 
        }

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.close = function() {
            $uibModalInstance.close('');
        };



    });
