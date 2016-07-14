'use strict';

angular.module('moviespaceApp')
    .controller('SpaceController', function($scope, $timeout, Principal, $state, $stateParams, AlertService, $uibModal, MovieService,PersonService,CompanyService,CollectionService) {

   MovieService.getTotalMovieCount().success(function(response){
   		console.log(response);
   		$scope.totalMovies = response; 
    }).error(function(){
    	$scope.totalMovies = 0;
    });

    PersonService.getTotalPeopleCount().success(function(response){
   		console.log(response);
   		$scope.totalPeople = response; 
    }).error(function(){
    	$scope.totalPeople = 0;
    });

    CollectionService.getTotalCollectionCount().success(function(response){
   		console.log(response);
   		$scope.totalCollections = response; 
    }).error(function(){
    	$scope.totalCollections = 0;
    });

    CompanyService.getTotalCompanyCount().success(function(response){
   		console.log(response);
   		$scope.totalCompanies = response; 
    }).error(function(){
    	$scope.totalCompanies = 0;
    });

});
