
yachtApp.controller('gameController', function(APIService, $scope, $routeParams, $window, $location) {

    $scope.game;

    // get the character from the provided id
    ($scope.fetchGame = function() {
        APIService.getGame($routeParams.gameId, function(response) {
           $scope.game = response.data;
        });
    })();

    $scope.reroll = function() {
        APIService.reroll($routeParams.gameId, $scope.game.dieRolls, function(response) {
            $scope.fetchGame();
        });
    }

    $scope.updateGameSheet = function() {
        $scope.fetchGame();
    }

});
