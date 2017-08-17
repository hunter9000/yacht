

angular.module('yachtApp').controller('materialsController', function(APIService, $scope, $http, $window, $routeParams, $uibModal, $location) {

    $scope.mats = null;

    $scope.fetchMats = function() {
        APIService.getMaterials(function(response) {
            $scope.mats = response.data;
        });

//        $http({method:'GET',
//               url: 'api/materials/',
//               headers: {'x-access-token': $window.localStorage['jwtToken']}
//            })
//            .success(function (data) {
//                $scope.mats = data;
//                console.log(data);
//            })
//            .error(function(data) {
//                console.log('Error:' + data);
//            }
//        );
    }
    $scope.fetchMats();      // kick off the chain of loading all the data. Load the effect types, then equip slots, then mats. this is so when we populate the mats, the other two are already loaded.

    $scope.addNewMat = function() {
        var modalInstance = $uibModal.open({
            animation: false,
            templateUrl: 'pages/templates/material-edit-template.html',
            controller: 'editMaterialController',
            size: 'md',
            resolve: {
                mat: function () {
                    return null;    // pass the character to delete
                }
            }
        });

        modalInstance.result.then(function (mat) {
            APIService.createMaterial(mat, function(response) {
                $scope.fetchMats();
            });

//            $http.post('/api/materials/',
//               mat,
//               { headers: {'x-access-token': $window.localStorage['jwtToken']} }
//            ).then(function successCallback(response) {
//                    console.log(response);
//                    $scope.fetchMats();
//                }, function errorCallback(response) {
//                    console.log('Error: ' + response);
//                    $location.path('/error');
//                }
//            );
        }, function () {
            console.log('Modal dismissed at: ' + new Date());
        });
    }

});

// controller for the modal window
yachtApp.controller('editMaterialController', function ($scope, $uibModalInstance, mat) {
    $scope.mat = mat;

    if ($scope.mat == null) {
        $scope.mat = {effectList: []};      // initialize the array of effects so it can be added to
    }

    $scope.newType;
    $scope.newValue;
    $scope.newSlot;

    $scope.addEffect = function() {
        var newEff = {'effectType': $scope.newType, 'value': $scope.newValue, 'slot': $scope.newSlot};
        $scope.mat.effectList.push(newEff);
        $scope.newType = null;
        $scope.newValue = null;
        $scope.newSlot = null;
        return false;
    }

    $scope.submitForm = function () {
        $uibModalInstance.close($scope.mat);
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
