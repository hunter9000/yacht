

yachtApp.controller('profileController', function(APIService, $scope, $routeParams, $window, $location){
	$scope.user = {};

    $scope.roles = [];

    APIService.getProfile(function(response) {
        $scope.user = response.data;
    });

    $scope.save = function() {
        APIService.editProfile($scope.user, function(response) {
            $scope.user = response.data;
        });
    }

});