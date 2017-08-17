
yachtApp.controller('gameSelectController', function(APIService, $scope, $window, $uibModal, $location) {
    $scope.message;// = 'token: ' + $window.localStorage['jwtToken'];

    $scope.games = [];

    ($scope.fetchGames = function() {
        APIService.getGames(function(response) {
            $scope.games = response.data;
        });
    })();

    //$scope.fetchGames();

    $scope.newGame = function() {
        APIService.createGame(function(response) {
            $scope.selectGame(response.data);
        });
    }

    $scope.selectGame = function(gameId) {
        $location.path("/game/" + gameId);
    }

    $scope.deleteGame = function(gameId) {
        APIService.deleteGame(gameId, function(response) {
            $scope.fetchGames();
        });
    }

});
