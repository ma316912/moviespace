'use strict';

angular.module('moviespaceApp')
    .controller('SearchController', function ($scope, $location, $state, Auth, Principal, $uibModal, ENV, SearchService,AlertService,Movie,Person,PersonService,CompanyService) {

        var vm = this;

        $scope.searchObj = {};
        $scope.flag = false;

        $scope.results = [];

        $scope.search = function() {
            switch($scope.searchObj.type) {
                case "movie":
                                SearchService.getMoviesByQuery($scope.searchObj.query).success(function(response){
                                    $scope.results = response;
                                    $scope.flag = true;
                                }).error(function(){
                                    $scope.results = [];
                                }); 
                                break;
                case "person": SearchService.getPersonsByQuery($scope.searchObj.query).success(function(response){
                                    $scope.results = response;
                                    $scope.flag = true;
                                }).error(function(){
                                    $scope.results = [];
                                });
                                break;
                case "company": SearchService.getCompaniesByQuery($scope.searchObj.query).success(function(response){
                                    $scope.results = response;
                                    $scope.flag = true;
                                }).error(function(){
                                    $scope.results = [];
                                });
                                break;
                case "collection": SearchService.getCollectionsByQuery($scope.searchObj.query).success(function(response){
                                    $scope.results = response;
                                    $scope.flag = true;
                                }).error(function(){
                                    $scope.results = [];
                                });
                                break;                                
            }
            console.log("Searching for ",$scope.searchObj.type);
            console.log($scope.results);
        };


        $scope.$watch("searchObj.type",function(oldValue,newValue){
            if(oldValue!=newValue)
                $scope.flag=false;
        });

        $scope.addMovieToMySpace = function(movie) {
            var m = angular.copy(movie);
            //m.belongsToCollectionId = $scope.collectionInfo.id;
            m.externalId = m.id;
            m.id = null;
            AlertService.info("Adding movie to MySpace.. Adding movie fetches crew,cast,review information from external interface. You'll be notified when the process completes..");
            Movie.save(m,function(response){
            },function(error){
                console.log(error);
            });
        };

        $scope.addPersonToMySpace = function(person) {
            var p = angular.copy(person);
            //m.belongsToCollectionId = $scope.collectionInfo.id;
            p.externalId = p.id;
            p.id = null;
            AlertService.info("Adding person to MySpace..");
            PersonService.addPersonToMySpace(p.externalId).success(function(){

            }).error(function(){

            });
        };

        $scope.addCompanyToMySpace = function(company) {
            AlertService.info("Adding company to MySpace..");
            CompanyService.addCompanyToMySpace(company.id).success(function(){
            }).error(function(){
            });
        };

        $scope.viewCollection = function(collection) {
                $uibModal.open({
                    templateUrl: 'scripts/app/search/collections/collection-dialog.html',
                    controller: 'CollectionInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        collectionInfo: function (SearchService) {
                            return SearchService.getCollectionInfoById(collection.id);
                        }
                    }
                }).result.then(function() {
                    AlertService.info("Adding collection involves fetching cast, crew , reviews for all the parts of a Collection. So we will notify you once the process completes.");
                    $state.go('collection', null, { reload: true });
                }, function() {
                    $state.go('search');
                });

            };


    })
    .controller('CollectionInfoDialogController',function($scope,$state,$uibModalInstance,SearchService,collectionInfo,Movie,Collection,CollectionService){
        var vm = this;

        $scope.collectionInfo = collectionInfo.data;
        console.log($scope.collectionInfo);

        $scope.addMovieToMySpace = function(movie) {
            var m = angular.copy(movie);
            m.belongsToCollectionId = $scope.collectionInfo.id;
            m.externalId = m.id;
            m.id = null;
            Movie.save(m,function(response){
                vm.clear();
            },function(error){
                console.log("error");
                console.log(error);
            });
        };


        $scope.addCollectionToMySpace = function(collection) {
            console.log("Here");
            CollectionService.addCollectionToMySpace(collection.id).success(function(response){
            }).error(function(){
            });
            vm.clear();
        };

        vm.clear = function() {
            $uibModalInstance.close('close');
        };

    });
