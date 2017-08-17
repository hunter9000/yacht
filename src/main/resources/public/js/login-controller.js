
// create the controller and inject Angular's $scope
yachtApp.controller('loginController', function(APIService, $scope, $location, $window) {
    // create a message to display in our view
    $scope.message = '';

    $scope.formData = {};

    // when submitting the add form, send the text to the node API
    $scope.login = function() {
        APIService.authenticate($scope.formData, function successCallback(response) {
            console.log(response);
            $window.localStorage['jwtToken'] = response.data.response;
            $location.path("/gameselect");
        });

//        $http.post('/api/authenticate/', $scope.formData)
//        .then(function successCallback(response) {
//            console.log(response);
//            $window.localStorage['jwtToken'] = response.data.response;
//            $location.path("/charselect");
//        }, function errorCallback(response) {
//            console.log('Error: ' + response);
//            $scope.message = 'invalid login - error returned';
//        });
    };
});