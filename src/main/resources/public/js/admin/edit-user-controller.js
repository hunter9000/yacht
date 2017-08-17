
yachtApp.controller('editUserController', function(APIService, $scope, $window, $location, $routeParams) {

    $scope.user = {};

    $scope.roles = [];

    APIService.getAllRoles(function(response) {
        $scope.roles = response.data;
    });
//    $http.get('/api/roles/', {
//        headers: {'x-access-token': $window.localStorage['jwtToken']}
//    })
//    .then(function successCallback(response) {
//        $scope.roles = response.data;
//    }, function errorCallback(response) {
//        console.log('error getting roles');
//        $location.path('/error');
//    });

    APIService.getUser($routeParams.userId, function(response) {
        $scope.user = response.data;
    });
//    $http.get('/api/users/' + $routeParams.userId + '/', {
//        headers: {'x-access-token': $window.localStorage['jwtToken']}
//    })
//    .then(function successCallback(response) {
//        $scope.user = response.data;
//    }, function errorCallback(response) {
//        console.log('error getting roles');
//        $location.path('/error');
//    });


    $scope.save = function() {
        APIService.updateUser($routeParams.userId, $scope.user, function(response) {
            $location.path('/users');
        });
//        $http.put('/api/users/' + $routeParams.userId + '/',
//            $scope.user,
//            { headers: {'x-access-token': $window.localStorage['jwtToken']} }
//        )
//        .then(function successCallback(response) {
////            $scope.roles = response.data;
//            $location.path('/users');
//        }, function errorCallback(response) {
//            console.log('error getting roles');
//            $location.path('/error');
//        });
    }

});